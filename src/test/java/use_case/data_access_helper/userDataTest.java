package use_case.data_access_helper;

import data_access.JsonUserDataAccessObject;
import entity.CommonUser;
import entity.SecurityUser;
import entity.User;
import org.junit.jupiter.api.*;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class userDataTest {

    private static Path tempDir;

    @BeforeAll
    static void setupClass() throws IOException {
        tempDir = Files.createTempDirectory("user_data_test");
    }

    @AfterAll
    static void tearDownClass() throws IOException {
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void testUpdateThrowsExceptionOnNullInputs() throws IOException {
        File file = tempDir.resolve("test1.json").toFile();
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject(file.getPath());

        assertThrows(IllegalArgumentException.class, () -> dao.update(null, new CommonUser("abc","a")));
        assertThrows(IllegalArgumentException.class, () -> dao.update("abc", null));
    }

    @Test
    void testSnapshotReturnsUnmodifiableMap() throws IOException {
        File file = tempDir.resolve("test2.json").toFile();
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject(file.getPath());

        dao.save(new CommonUser("u1","1"));
        Map<String, User> snapshot = dao.snapshot();

        assertEquals("u1", snapshot.get("u1").getName());
        assertThrows(UnsupportedOperationException.class, () -> snapshot.put("x", new CommonUser("x","1")));
    }

    @Test
    void testPersistThrowsRuntimeException() throws Exception {
        File file = tempDir.resolve("test3.json").toFile();
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject(file.getPath());
        dao.save(new CommonUser("bad","111"));

        // 破坏 store 字段使其无法写入
        Field storeField = JsonUserDataAccessObject.class.getDeclaredField("store");
        storeField.setAccessible(true);
        storeField.set(dao, new File("/invalid/path/that/does/not/exist.json"));

        assertThrows(RuntimeException.class, () -> dao.save(new CommonUser("crash","111")));
    }

    @Test
    void testThrowsIOExceptionWhenParentDirCannotBeCreated() {
        // NUL 是非法的 Windows 文件夹名，Linux/macOS 请改成 /dev/null
        File file = new File("NUL/test.json");
        assertThrows(IOException.class, () -> new JsonUserDataAccessObject(file.getPath()));
    }

    @Test
    void testLoadFromResourceFile() throws IOException {
        // 加载 src/test/resources/data/user_data.json
        String path = Paths.get("src/test/resources/data/user_data.json").toAbsolutePath().toString();
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject(path);

        // 即使数据为空也能构造
        assertNotNull(dao);
    }

    @Test
    void testDefaultConstructor() throws IOException {
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject(); // 使用默认路径
        assertNotNull(dao);
    }

    @Test
    void testSecurityUserMethods() throws IOException {
        File file = tempDir.resolve("test4.json").toFile();
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject(file.getPath());

        SecurityUser user = new SecurityUser("sec","111", "Q?", "A!");
        dao.save(user);

        assertEquals("SECURITY", dao.getType("sec"));
        assertEquals("Q?", dao.getSecurityQuestion("sec"));
        assertEquals("A!", dao.getAnswer("sec"));
        assertEquals("Q?", dao.getQuestion("sec"));
    }

    @Test
    void testCommonUserType() throws IOException {
        File file = tempDir.resolve("test5.json").toFile();
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject(file.getPath());

        CommonUser user = new CommonUser("com","111");
        dao.save(user);
        assertEquals("COMMON", dao.getType("com"));
    }

    @Test
    void testSetAndGetCurrentUsername() throws IOException {
        File file = tempDir.resolve("test6.json").toFile();
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject(file.getPath());

        dao.setCurrentUsername("userX");
        assertEquals("userX", dao.getCurrentUsername());
    }
}
