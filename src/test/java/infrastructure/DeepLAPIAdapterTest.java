package infrastructure;

import entity.Language;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeepLAPIAdapterTest {

    private HttpClient mockClient;
    private HttpResponse<String> mockResp;
    private MockedStatic<HttpClient> httpMock;

    @BeforeAll
    void setUpStaticMock() throws Exception {
        mockClient = Mockito.mock(HttpClient.class);
        mockResp   = Mockito.mock(HttpResponse.class);

        httpMock = mockStatic(HttpClient.class);
        httpMock.when(HttpClient::newHttpClient).thenReturn(mockClient);

        when(mockClient.send(any(HttpRequest.class),
                any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResp);

        when(mockResp.statusCode()).thenReturn(200);
        when(mockResp.body()).thenReturn(
                "{\"translations\":[{\"detected_source_language\":\"EN\",\"text\":\"apple_zh\"}]}"
        );
    }

    @AfterAll
    void tearDownStaticMock() {
        httpMock.close();
    }

    @Test
    void getTranslation_returnsTranslatedText() {
        DeepLAPIAdapter adapter = new DeepLAPIAdapter();
        String zh = adapter.getTranslation("apple", Language.ZH);
        assertEquals("apple_zh", zh);
    }

    @Test
    void getTranslation_blankInput_returnsEmpty() {
        DeepLAPIAdapter adapter = new DeepLAPIAdapter();
        assertEquals("", adapter.getTranslation("   ", Language.ZH));
    }

    @Test
    void parseTranslatedText_extractsFirstTextValue() throws Exception {
        String json = "{\"translations\":[{\"detected_source_language\":\"EN\",\"text\":\"你好\"}]}";
        var m = DeepLAPIAdapter.class.getDeclaredMethod("parseTranslatedText", String.class);
        m.setAccessible(true);
        assertEquals("你好", m.invoke(null, json));
    }

    @Test
    void getTranslation_throws_whenStatusNot200() throws Exception {

        @SuppressWarnings("unchecked")
        HttpResponse<String> errorResp = Mockito.mock(HttpResponse.class);

        when(errorResp.statusCode()).thenReturn(456);
        when(errorResp.body()).thenReturn("quota exceeded");

        when(mockClient.send(any(HttpRequest.class),
                any(HttpResponse.BodyHandler.class)))
                .thenReturn(errorResp);

        DeepLAPIAdapter adapter = new DeepLAPIAdapter();

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adapter.getTranslation("apple", Language.ZH));

        assertTrue(ex.getMessage() != null
                        && ex.getMessage().startsWith("DeepL returned HTTP 456"),
                "456");
    }

    @Test
    void getTranslation_wrapsIOException() throws Exception {
        when(mockClient.send(any(HttpRequest.class),
                any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("network down"));

        DeepLAPIAdapter adapter = new DeepLAPIAdapter();

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adapter.getTranslation("apple", Language.ZH));

        assertTrue(ex.getMessage().startsWith("DeepL request failed"));
    }

    @Test
    void getTranslation_wrapsInterruptedException_and_setsFlag() throws Exception {
        when(mockClient.send(any(HttpRequest.class),
                any(HttpResponse.BodyHandler.class)))
                .thenThrow(new InterruptedException("boom"));

        DeepLAPIAdapter adapter = new DeepLAPIAdapter();

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adapter.getTranslation("apple", Language.ZH));

        assertTrue(ex.getMessage().startsWith("DeepL request failed"));

        assertTrue(Thread.currentThread().isInterrupted(),
                "interrupted");

        Thread.interrupted();
    }

    @Test
    public void parseTranslatedText_coversMissingMarker_andEmptyText() throws Exception {
        // Access private static method parseTranslatedText(String)
        Method m = DeepLAPIAdapter.class.getDeclaredMethod("parseTranslatedText", String.class);
        m.setAccessible(true);

        // 1) Branch: start < 0  -> returns ""
        String withoutMarker = "{}";
        String r1 = (String) m.invoke(null, withoutMarker);
        assertEquals("", r1);

        // 2) Branch: marker found but value is empty -> end == start -> returns ""
        //    JSON includes the marker \"text\":\"\" so start >= 0 and next quote is immediately at 'start'.
        String emptyValue = "{\"translations\":[{\"text\":\"\"}]}";
        String r2 = (String) m.invoke(null, emptyValue);
        assertEquals("", r2);
    }

}
