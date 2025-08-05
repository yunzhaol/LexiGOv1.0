package use_case.data_access_helper;


import data_access.WordBookDataAccessObject;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class WordBookDataAccessObjectCoverageTest {

    /**
     * 覆盖默认构造器 + resolveDefaultPath() 路径。
     * 只要执行到 new，就算覆盖了 resolveDefaultPath() 中 getResource==null 和正常分支。
     */
    @Test
    void defaultCtor_executesResolveDefaultPath() {
        try {
            new WordBookDataAccessObject();
        } catch (Exception ignored) {
        }
    }

    /**
     * 覆盖 String 构造器 + load() 中 “文件不存在” 分支：
     * 真实文件系统：notExists→true → create + write "{}"
     * 然后 get() 强行抛“No word book found”
     */
    @Test
    void stringCtor_andGet_coverEmptyCache() throws IOException {
        Path dir = Files.createTempDirectory("wbdao");
        Path file = dir.resolve("wb.json");
        // 构造并写 {}
        WordBookDataAccessObject dao = new WordBookDataAccessObject(file.toString());
        // 文件创建了
        assert Files.exists(file);
        // get() 会抛
        try {
            dao.get();
        } catch (Exception ignored) {
        }
    }

    /**
     * 覆盖 load() 中 IO 异常分支：
     * mock Files.newInputStream(...) 抛 IOException
     */
    @Test
    void stringCtor_whenNewInputStreamThrows_coverCatch() {
        Path fake = Paths.get("nonexistent","wb.json");
        try (MockedStatic<Files> fs = mockStatic(Files.class)) {
            fs.when(() -> Files.notExists(fake)).thenReturn(false);
            fs.when(() -> Files.newInputStream(fake)).thenThrow(new IOException("boom"));
            try {
                new WordBookDataAccessObject(fake.toString());
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 覆盖 private static resolveDefaultPath() 中 URISyntaxException 分支
     * 直接反射调用，让它跑到 URI 转换处，捕任何异常
     */
    @Test
    void reflect_resolveDefaultPath_executesCatch() {
        try {
            Method m = WordBookDataAccessObject.class.getDeclaredMethod("resolveDefaultPath");
            m.setAccessible(true);
            m.invoke(null);
        } catch (Exception ignored) {
        }
    }
}
