package use_case.data_access_helper;

import data_access.JsonUserDataAccessObject;
import entity.CommonUser;
import entity.SecurityUser;
import entity.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 覆盖目标：
 * - if (!parent.exists() && !parent.mkdirs()) { throw new IOException(...) }
 * - else if (!store.createNewFile()) { throw new IOException(...) }
 * - 默认构造函数
 * - update() 的空参数校验
 * - snapshot() 的不可修改性
 * - persist() 内部 IOException -> RuntimeException 的包装
 * - 常见 getter / setter 分支 (getType/getQuestion/getAnswer等)
 */
@ExtendWith(MockitoExtension.class)
// 防止偶发的 UnnecessaryStubbingException（这里主要是为了稳）
@MockitoSettings(strictness = Strictness.LENIENT)
class userData2Test {

    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("user_dao_test_");
    }

    @AfterEach
    void tearDown() throws IOException {
        // 递归删除
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    // =============== 关键分支 1：父目录创建失败 ===============
    @Test
    void ctor_throwsIOException_whenParentMkdirsFails() {
        try (MockedConstruction<File> mocked = mockConstruction(File.class, (storeMock, context) -> {
            // parent mock
            File parentMock = mock(File.class);
            when(storeMock.getParentFile()).thenReturn(parentMock);
            // 让 parent 不存在，且 mkdirs() 失败
            when(parentMock.exists()).thenReturn(false);
            when(parentMock.mkdirs()).thenReturn(false);
        })) {
            IOException ex = assertThrows(IOException.class,
                    () -> new JsonUserDataAccessObject("C:/whatever/path/users.json"));
            assertTrue(ex.getMessage().startsWith("Cannot create data dir"));
        }
    }

    // =============== 关键分支 2：createNewFile() 返回 false ===============
    @Test
    void ctor_throwsIOException_whenCreateNewFileReturnsFalse() {
        try (MockedConstruction<File> mocked = mockConstruction(File.class, (storeMock, context) -> {
            // parent 存在 -> 不触发第一分支
            File parentMock = mock(File.class);
            when(storeMock.getParentFile()).thenReturn(parentMock);
            when(parentMock.exists()).thenReturn(true);

            // 文件当前不存在 -> 会走 createNewFile()
            when(storeMock.exists()).thenReturn(false);
            // 模拟 createNewFile() 返回 false -> 命中 else-if 分支
            when(storeMock.createNewFile()).thenReturn(false);
        })) {
            IOException ex = assertThrows(IOException.class,
                    () -> new JsonUserDataAccessObject("D:/no_matter/users.json"));
            assertTrue(ex.getMessage().startsWith("Cannot create "));
        }
    }

    // =============== 默认构造函数（只为覆盖那一行） ===============
    @Test
    void defaultConstructor_justConstructs() throws IOException {
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject();
        assertNotNull(dao);
        // 不做更多断言（只是为覆盖 DEFAULT_FILE_PATH 构造器）
    }

    // =============== 使用真实文件：基础功能 + snapshot 不可变 ===============
    @Test
    void snapshot_isUnmodifiable_and_basicOps() throws IOException {
        Path file = tempDir.resolve("users.json");
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject(file.toString());

        dao.save(new CommonUser("u1","111"));
        dao.save(new SecurityUser("sec","111", "Q?", "A!"));

        assertTrue(dao.existsByName("u1"));
        assertEquals("u1", dao.get("u1").getName());

        assertEquals("COMMON", dao.getType("u1"));
        assertEquals("SECURITY", dao.getType("sec"));
        assertEquals("Q?", dao.getSecurityQuestion("sec"));
        assertEquals("A!", dao.getAnswer("sec"));
        assertEquals("Q?", dao.getQuestion("sec"));

        dao.setCurrentUsername("u1");
        assertEquals("u1", dao.getCurrentUsername());

        Map<String, User> snap = dao.snapshot();
        assertEquals(2, snap.size());
        assertThrows(UnsupportedOperationException.class, () -> snap.put("x", new CommonUser("x","1")));
    }

    // =============== update() 空参数校验 ===============
    @Test
    void update_throwsOnNullArgs() throws IOException {
        Path file = tempDir.resolve("update.json");
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject(file.toString());

        assertThrows(IllegalArgumentException.class, () -> dao.update(null, new CommonUser("a", "1")));
        assertThrows(IllegalArgumentException.class, () -> dao.update("a", null));
    }

    // =============== persist() catch 分支：把 IOException 包成 RuntimeException ===============
    @Test
    void persistIOException_isWrappedAsRuntimeException() throws Exception {
        Path file = tempDir.resolve("persist.json");
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject(file.toString());
        dao.save(new CommonUser("ok", "1")); // 正常一次，确保 map 有内容

        // 将内部的 'store' 字段改成一个目录，让 FileWriter 打开失败 -> 触发 catch
        Path dirAsStore = Files.createTempDirectory(tempDir, "as_store_dir");
        Field storeField = JsonUserDataAccessObject.class.getDeclaredField("store");
        storeField.setAccessible(true);
        storeField.set(dao, dirAsStore.toFile());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> dao.save(new CommonUser("boom", "1")));
        assertTrue(ex.getMessage().contains("Failed to write user DB"));
    }

    // =============== 读取 test 资源（只为覆盖 load 分支也能走到） ===============
    @Test
    void canConstructFromTestResource() throws IOException {
        String resourcePath = Paths.get("src/test/resources/data/user_data.json")
                .toAbsolutePath().toString();
        // 确保文件存在
        File f = new File(resourcePath);
        if (!f.getParentFile().exists()) {
            assertTrue(f.getParentFile().mkdirs());
        }
        if (!f.exists()) {
            assertTrue(f.createNewFile());
            Files.writeString(f.toPath(), "{}");
        }
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject(resourcePath);
        assertNotNull(dao);
    }
}
