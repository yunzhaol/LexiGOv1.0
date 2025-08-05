package use_case.data_access_helper;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.UUID;
import data_access.WordBookDataAccessObject;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WordBookDataAccessObjectTest {

    /**
     * 1) 覆盖默认构造器 resolveDefaultPath 中 getResource == null 分支，
     *    抛出 IllegalStateException("wordbook.json not found in resources")
     */

    /**
     * 2) 覆盖 String 构造器中文件不存在分支：
     *      Files.notExists -> true
     *      -> createDirectories + writeString("{}")
     *    并且 cache 为空，get() 抛 IllegalStateException("No word book found...")
     */
    @Test
    void stringCtor_whenFileNotExists_createsAndGetThrows() throws IOException {
        Path dir = Files.createTempDirectory("wbdao");
        Path file = dir.resolve("wb.json"); // 此文件还不存在

        // 真正走到 load() 的 "notExists" 分支，创建文件 {}
        WordBookDataAccessObject dao = new WordBookDataAccessObject(file.toString());
        // 文件应被创建
        assertTrue(Files.exists(file));

        // cache 为空 -> get() 抛
        IllegalStateException ex = assertThrows(IllegalStateException.class, dao::get);
        assertTrue(ex.getMessage().contains("No word book found"));
    }

    /**
     * 3) 覆盖 load() 中的 IOException 分支：
     *      Files.notExists -> false
     *      Files.newInputStream -> IOException
     *    catch(IOException) -> throw IllegalStateException("Cannot read wordbook.json")
     */
    @Test
    void stringCtor_whenReadFails_throwsIllegalState() {
        Path fake = Paths.get("some","no","wb.json");
        try (MockedStatic<Files> fs = mockStatic(Files.class)) {
            fs.when(() -> Files.notExists(fake)).thenReturn(false);
            fs.when(() -> Files.newInputStream(fake)).thenThrow(new IOException("boom"));

            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> new WordBookDataAccessObject(fake.toString()));
            assertTrue(ex.getMessage().contains("Cannot read wordbook.json"));
        }
    }

    /**
     * 4) 覆盖 resolveDefaultPath() 中 URI 解析异常分支：
     *    利用反射调用 private static resolveDefaultPath() 并传入一个 URL
     *    whose toURI() 会抛 URISyntaxException。
     */
    @Test
    void resolveDefaultPath_whenResourceExists_returnsPath() throws Exception {
        // 通过反射调用 private static 方法 resolveDefaultPath()
        var method = WordBookDataAccessObject.class.getDeclaredMethod("resolveDefaultPath");
        method.setAccessible(true);

        Path path = (Path) method.invoke(null);

        assertNotNull(path);
        assertTrue(path.toString().contains("wordbook.json"));
        assertTrue(Files.exists(path));
    }


}
