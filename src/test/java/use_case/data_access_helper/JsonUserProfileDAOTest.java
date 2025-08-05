package data_access;

import com.google.gson.GsonBuilder;
import entity.Language;
import entity.PersonalProfile;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class JsonUserProfileDAOTest {

    Path tempDir;
    Path profilesFile;

    @BeforeEach
    void init() throws IOException {
        tempDir = Files.createTempDirectory("dao_test_");
        profilesFile = tempDir.resolve("profiles.json");
    }

    @AfterEach
    void cleanup() throws IOException {
        // 删除临时目录
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(f -> { if (!f.delete()) f.deleteOnExit(); });
    }

    //——— cover: public JsonUserProfileDAO(String dirPath) -> ensureDirectoryExists + ensureFileExists ———
    @Test
    void ctor_createsDirAndFile() {
        // mock Files.createDirectories + exists + writeString
        try (MockedStatic<Files> fs = mockStatic(Files.class)) {
            fs.when(() -> Files.createDirectories(any(Path.class)))
                    .thenReturn(tempDir);
            fs.when(() -> Files.exists(profilesFile)).thenReturn(false);
            fs.when(() -> Files.writeString(eq(profilesFile), eq("{}"), eq(StandardCharsets.UTF_8)))
                    .thenReturn(profilesFile);

            JsonUserProfileDAO dao = new JsonUserProfileDAO(tempDir.toString());
            assertNotNull(dao);
        }
    }

    //——— cover: default ctor ———
    @Test
    void defaultCtor_runs() {
        try (MockedStatic<Files> fs = mockStatic(Files.class)) {
            fs.when(() -> Files.createDirectories(any(Path.class))).thenReturn(tempDir);
            fs.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            // ensureFileExists won't write
            JsonUserProfileDAO dao = new JsonUserProfileDAO();
            assertNotNull(dao);
        }
    }

    //——— cover: save(null) -> IllegalArgumentException ———
    @Test
    void save_null_throws() throws IOException {
        JsonUserProfileDAO dao = new JsonUserProfileDAO(tempDir.toString());
        assertThrows(IllegalArgumentException.class, () -> dao.save(null));
    }

    //——— cover: getLanguage inserts default when missing ———
    @Test
    void getLanguage_missing_insertsDefault() throws IOException {
        // real FS: start fresh
        JsonUserProfileDAO dao = new JsonUserProfileDAO(tempDir.toString());
        // no profile "alice" yet
        Language lang = dao.getLanguage("alice");
        assertEquals(Language.FR, lang);
        // second call reads from file, still returns FR
        assertEquals(Language.FR, dao.getLanguage("alice"));
    }

    //——— cover: updateLanguage(null, null) throws ———
    @Test
    void updateLanguage_nullArgs_throws() throws IOException {
        JsonUserProfileDAO dao = new JsonUserProfileDAO(tempDir.toString());
        assertThrows(IllegalArgumentException.class, () -> dao.updateLanguage(null, Language.EN));
        assertThrows(IllegalArgumentException.class, () -> dao.updateLanguage("bob", null));
    }

    //——— cover: updateLanguage success path ———
    @Test
    void updateLanguage_changesLanguage() throws IOException {
        JsonUserProfileDAO dao = new JsonUserProfileDAO(tempDir.toString());
        dao.updateLanguage("bob", Language.EN);
        assertEquals(Language.EN, dao.getLanguage("bob"));
    }

    //——— cover: ensureDirectoryExists IOException -> IllegalStateException ———
    @Test
    void ctor_whenMkdirsFails_throwsIllegalState() {
        try (MockedStatic<Files> fs = mockStatic(Files.class)) {
            fs.when(() -> Files.createDirectories(any(Path.class)))
                    .thenThrow(new IOException("fail mkdir"));

            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> new JsonUserProfileDAO(tempDir.resolve("nonexistent").toString()));
            assertTrue(ex.getMessage().startsWith("Unable to create data directory"));
        }
    }

    //——— cover: readAll IOException -> RuntimeException ———
    @Test
    void readAll_whenReadStringFails_throwsRuntime() throws IOException {
        try (MockedStatic<Files> fs = mockStatic(Files.class)) {
            // for ctor
            fs.when(() -> Files.createDirectories(any(Path.class))).thenReturn(tempDir);
            fs.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            // readAll path
            fs.when(() -> Files.readString(any(Path.class), any()))
                    .thenThrow(new IOException("fail read"));

            JsonUserProfileDAO dao = new JsonUserProfileDAO(tempDir.toString());
            RuntimeException ex = assertThrows(RuntimeException.class, () -> dao.getLanguage("x"));
            assertTrue(ex.getMessage().startsWith("Failed to read profiles"));
        }
    }

    //——— cover: writeAll IOException -> RuntimeException ———
    // 1) 确保 ctor 在写文件失败时抛 IllegalStateException
    @Test
    void ctor_whenWriteStringFails_throwsIllegalState() {
        // 指定一个临时目录
        Path tempDir = Paths.get("does/not/matter");
        Path profilesJson = tempDir.resolve("profiles.json");

        try (MockedStatic<Files> fs = mockStatic(Files.class)) {
            // 拦截目录创建，假装成功
            fs.when(() -> Files.createDirectories(any(Path.class)))
                    .thenReturn(tempDir);

            // 拦截 exists，首次调用（ensureFileExists）返回 false
            fs.when(() -> Files.exists(profilesJson))
                    .thenReturn(false);

            // 拦截写文件，让它抛 IOException
            fs.when(() -> Files.writeString(
                            eq(profilesJson),
                            anyString(),
                            eq(StandardCharsets.UTF_8)))
                    .thenThrow(new IOException("disk full"));

            // 构造时会走 ensureFileExists 并因 writeString 抛出被包成 IllegalStateException
            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> new JsonUserProfileDAO(tempDir.toString()));
            assertTrue(ex.getMessage().startsWith("Unable to create profile json"));
        }
    }

    // 2) 确保 save(...) 在写入失败时抛 RuntimeException
    @Test
    void writeAll_whenWriteStringFails_throwsRuntime() throws IOException {
        Path tempDir = Paths.get("unused");
        Path profilesJson = tempDir.resolve("profiles.json");

        try (MockedStatic<Files> fs = mockStatic(Files.class)) {
            // 构造阶段：目录存在，文件存在，readString 返回空 map
            fs.when(() -> Files.createDirectories(any(Path.class)))
                    .thenReturn(tempDir);
            fs.when(() -> Files.exists(any(Path.class)))
                    .thenReturn(true);
            fs.when(() -> Files.readString(
                            eq(profilesJson),
                            eq(StandardCharsets.UTF_8)))
                    .thenReturn("{}");

            // save 阶段：拦截写文件，抛 IOException
            fs.when(() -> Files.writeString(
                            eq(profilesJson),
                            anyString(),
                            eq(StandardCharsets.UTF_8)))
                    .thenThrow(new IOException("cannot write"));

            JsonUserProfileDAO dao = new JsonUserProfileDAO(tempDir.toString());
            // 这里 save(...) 会触发 writeAll -> writeString -> IOException -> 被包成 RuntimeException
            RuntimeException rex = assertThrows(RuntimeException.class,
                    () -> dao.save(new PersonalProfile("john", Language.EN)));
            assertTrue(rex.getMessage().startsWith("Failed to write profiles"));
        }
    }
}
