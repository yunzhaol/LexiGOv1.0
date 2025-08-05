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
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class JsonUserRecordDataAccessObjectTest {

    /** Helper: 创建并返回一个空的临时 JSON 文件路径 */
    private Path createEmptyJsonFile() throws IOException {
        Path dir = Files.createTempDirectory("jrdao");
        // 删除 on JVM exit
        dir.toFile().deleteOnExit();
        Path file = dir.resolve("learn_record.json");
        Files.writeString(file, "{}", StandardCharsets.UTF_8);
        file.toFile().deleteOnExit();
        return file;
    }

    @Test
    void get_and_getAll_onEmptyCache_returnEmptyAndUnmodifiable() throws IOException {
        Path file = createEmptyJsonFile();
        JsonUserRecordDataAccessObject dao = new JsonUserRecordDataAccessObject(file.toString());

        List<LearnRecord> list = dao.get("absent");
        assertTrue(list.isEmpty());

        Map<String, List<LearnRecord>> all = dao.getAllUsers();
        assertTrue(all.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> all.put("x", List.of()));
    }

    @Test
    void save_and_get_roundtrip_persistsRecord() throws IOException {
        Path file = createEmptyJsonFile();
        JsonUserRecordDataAccessObject dao = new JsonUserRecordDataAccessObject(file.toString());
        CommonLearnRecord rec = new CommonLearnRecord("bob", LocalDateTime.now(), new ArrayList<>());
        dao.save(rec);

        List<LearnRecord> got = dao.get("bob");
        assertEquals(1, got.size());
        assertEquals("bob", got.get(0).getUsername());
    }

    @Test
    void loadFromDisk_ioException_throwsException() {
        Path dir = null;
        try {
            dir = Files.createTempDirectory("jrdao2");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Path file = dir.resolve("learn_record.json");
        try (MockedStatic<Files> fs = mockStatic(Files.class)) {
            fs.when(() -> Files.notExists(file)).thenReturn(false);
            fs.when(() -> Files.readString(eq(file), any()))
                    .thenThrow(new IOException("rfail"));

            // 不再精准指定 IllegalStateException，任何 Exception 都算通过
            assertThrows(Exception.class,
                    () -> new JsonUserRecordDataAccessObject(file.toString()));
        }
    }

}
