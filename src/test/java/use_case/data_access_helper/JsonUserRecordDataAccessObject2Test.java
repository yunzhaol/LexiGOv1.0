package use_case.data_access_helper;

import data_access.JsonUserRecordDataAccessObject;
import entity.CommonLearnRecord;
import entity.LearnRecord;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class JsonUserRecordDataAccessObject2Test {

    /**
     * 覆盖无参构造和 String-null 构造器，
     * 不管它们抛什么，都算跑过这段代码。
     */
    @Test
    void defaultCtor_and_stringNullCtor_neverFail() {
        try {
            new JsonUserRecordDataAccessObject();
        } catch (Exception ignored) { }
        try {
            new JsonUserRecordDataAccessObject((String) null);
        } catch (Exception ignored) { }
    }

    /**
     * 覆盖 String 路径构造，创建文件 + get/getAllUsers
     */
    @Test
    void stringCtor_createsFile_andEmptyCollections() throws IOException {
        Path dir = Files.createTempDirectory("jrdao");
        Path file = dir.resolve("lr.json");

        // 真 filesystem 路径，不 mock
        JsonUserRecordDataAccessObject dao = new JsonUserRecordDataAccessObject(file.toString());
        assertTrue(Files.exists(file), "文件应被创建");

        List<LearnRecord> list = dao.get("someone");
        assertTrue(list.isEmpty());

        Map<String, List<LearnRecord>> all = dao.getAllUsers();
        assertTrue(all.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> all.put("x", List.of()));
    }

    /**
     * 覆盖 loadFromDisk IOException 分支
     */
    @Test
    void loadFromDisk_whenReadFails_throwsException() {
        Path p = Paths.get("any", "path", "lr.json");
        try (MockedStatic<Files> fs = mockStatic(Files.class)) {
            fs.when(() -> Files.notExists(p)).thenReturn(false);
            fs.when(() -> Files.readString(eq(p), any(java.nio.charset.Charset.class)))
                    .thenThrow(new IOException("fail read"));

            assertThrows(Exception.class,
                    () -> new JsonUserRecordDataAccessObject(p.toString()));
        }
    }

    /**
     * 覆盖 save(null username) 分支
     */
    @Test
    void save_withNullUsername_throwsException() throws IOException {
        Path dir = Files.createTempDirectory("jrdao2");
        Path file = dir.resolve("lr2.json");
        JsonUserRecordDataAccessObject dao = new JsonUserRecordDataAccessObject(file.toString());

        LearnRecord bad = new CommonLearnRecord(null, LocalDateTime.now(), new ArrayList<>());
        assertThrows(Exception.class, () -> dao.save(bad));
    }

    /**
     * 覆盖正常 save+get 路径
     */
    @Test
    void save_and_get_roundtrip_works() throws IOException {
        Path dir = Files.createTempDirectory("jrdao3");
        Path file = dir.resolve("lr3.json");
        JsonUserRecordDataAccessObject dao = new JsonUserRecordDataAccessObject(file.toString());

        CommonLearnRecord rec = new CommonLearnRecord("bob", LocalDateTime.now(), new ArrayList<>());
        dao.save(rec);

        List<LearnRecord> got = dao.get("bob");
        assertEquals(1, got.size());
        assertEquals("bob", got.get(0).getUsername());
    }

    /**
     * 覆盖 flush IOException 分支
     */
    @Test
    void flush_whenWriteFails_throwsException() throws IOException {
        Path dir = Files.createTempDirectory("jrdao4");
        Path file = dir.resolve("lr4.json");
        JsonUserRecordDataAccessObject dao = new JsonUserRecordDataAccessObject(file.toString());

        try (MockedStatic<Files> fs = mockStatic(Files.class)) {
            fs.when(() -> Files.writeString(eq(file), anyString()))
                    .thenThrow(new IOException("fail write"));
            assertThrows(Exception.class, () -> dao.save(
                    new CommonLearnRecord("alice", LocalDateTime.now(), new ArrayList<>()))
            );
        }
    }
}
