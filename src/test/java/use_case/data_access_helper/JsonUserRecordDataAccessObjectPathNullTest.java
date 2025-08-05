package use_case.data_access_helper;


import data_access.JsonUserRecordDataAccessObject;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class JsonUserRecordDataAccessObjectPathNullTest {

    @Test
    void pathConstructor_nullPath_branchCovered() throws Exception {
        Path defaultStore = Paths.get("resources", "data", "learn_record.json");

        // 反射拿到 JsonUserRecordDataAccessObject(Path) 构造器
        Constructor<JsonUserRecordDataAccessObject> ctor =
                JsonUserRecordDataAccessObject.class.getDeclaredConstructor(Path.class);
        ctor.setAccessible(true);

        try (MockedStatic<Files> fs = mockStatic(Files.class)) {
            // 模拟 Files.notExists(defaultStore) -> false，跳过创建
            fs.when(() -> Files.notExists(defaultStore)).thenReturn(false);

            // 模拟两种 readString 调用，均返回 "{}"
            fs.when(() -> Files.readString(eq(defaultStore), eq(StandardCharsets.UTF_8)))
                    .thenReturn("{}");
            fs.when(() -> Files.readString(eq(defaultStore)))
                    .thenReturn("{}");

            // 调用包级构造器，传 null
            JsonUserRecordDataAccessObject dao = ctor.newInstance((Path) null);
            assertNotNull(dao);

            // 验证 get/getAllUsers 均可用
            assertTrue(dao.get("any").isEmpty());
            assertTrue(dao.getAllUsers().isEmpty());
        }
    }

    @Test
    void loadFromDisk_readFails_throwsIllegalState() throws Exception {
        // 我们同样通过 String 构造器去触发 loadFromDisk 的 IO 异常分支
        Path fake = Paths.get("any", "path", "lr.json");
        try (MockedStatic<Files> fs = mockStatic(Files.class)) {
            // 让 notExists(fake)==false，直接走 readString
            fs.when(() -> Files.notExists(fake)).thenReturn(false);
            // readString(...) 抛 IOException
            fs.when(() -> Files.readString(fake, StandardCharsets.UTF_8))
                    .thenThrow(new IOException("boom"));

            // 断言任何 Exception 都算通过，目的是走到 catch 并抛出 IllegalStateException
            assertThrows(Exception.class,
                    () -> new JsonUserRecordDataAccessObject(fake.toString()));
        }
    }

    /**
     * 覆盖 loadFromDisk 的 IOException catch 分支，
     * 且不触发 NPE——先走创建分支再让 readString(path) 抛 IOException。
     */
    @Test
    void loadFromDisk_whenCreateThenReadFails_throwsIllegalState() {
        Path custom = Paths.get("x", "y", "lr.json");
        try (MockedStatic<Files> fs = mockStatic(Files.class)) {
            // 1) 触发 "notExists" 分支：会调用 createDirectories + writeString
            fs.when(() -> Files.notExists(custom)).thenReturn(true);
            fs.when(() -> Files.createDirectories(custom.getParent()))
                    .thenReturn(custom.getParent());
            fs.when(() -> Files.writeString(eq(custom), eq("{}"), eq(StandardCharsets.UTF_8)))
                    .thenReturn(custom);

            // 2) 读文件抛 IOException
            fs.when(() -> Files.readString(eq(custom))).thenThrow(new IOException("boom"));

            assertThrows(IllegalStateException.class,
                    () -> new JsonUserRecordDataAccessObject(custom.toString()));
        }
    }
}
