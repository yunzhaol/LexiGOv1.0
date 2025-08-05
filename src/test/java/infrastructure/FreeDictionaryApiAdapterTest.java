package infrastructure;

import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FreeDictionaryApiAdapterTest {

    @Mock private OkHttpClient mockClient;
    @Mock private Call         mockCall;
    @Mock private Response     mockResp;
    @Mock private ResponseBody mockBody;

    private FreeDictionaryApiAdapter adapter;

    @BeforeEach
    void setUp() throws Exception {
        adapter = new FreeDictionaryApiAdapter();

        Field f = FreeDictionaryApiAdapter.class.getDeclaredField("client");
        f.setAccessible(true);
        f.set(adapter, mockClient);

        lenient().when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
    }

    @Test
    void getWordExample_returnsSentence_whenExamplePresent() throws Exception {
        String json =
                "[{\"meanings\":[{\"definitions\":[{\"example\":\"hello there, Katie!\"}]}]}]";

        stubSuccessfulCall(json);

        String ex = adapter.getWordExample("hello");
        assertEquals("hello there, Katie!", ex);
    }

    @Test
    void getWordExample_returnsFallback_whenNoExample() throws Exception {
        String json =
                "[{\"meanings\":[{\"definitions\":[{}]}]}]";

        stubSuccessfulCall(json);

        String ex = adapter.getWordExample("test");
        assertEquals("No example found.", ex);
    }

    @Test
    void getWordExample_returnsMessage_whenInternetError() throws Exception {
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenThrow(new UnknownHostException("Unable to resolve host"));

        String result = adapter.getWordExample("test");

        assertEquals("Failed connecting to the endpoint", result);
    }

    @Test
    void getWordExample_throws_whenInternetError() throws Exception {
        when(mockResp.isSuccessful()).thenReturn(false);
        when(mockResp.code()).thenReturn(404);
        when(mockCall.execute()).thenReturn(mockResp);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> adapter.getWordExample("oops"));

        assertTrue(ex.getMessage().startsWith("Dictionary lookup failed"));
    }

    @Test
    void getWordExample_wrapsIOException() throws Exception {
        when(mockCall.execute()).thenThrow(new IOException("network down"));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> adapter.getWordExample("io"));

        assertTrue(ex.getMessage().startsWith("Dictionary lookup failed"));
    }

    private void stubSuccessfulCall(String json) throws Exception {
        when(mockCall.execute()).thenReturn(mockResp);
        when(mockResp.isSuccessful()).thenReturn(true);
        when(mockResp.body()).thenReturn(mockBody);

        var bytes = json.getBytes(StandardCharsets.UTF_8);
        when(mockBody.byteStream()).thenReturn(new ByteArrayInputStream(bytes));
    }
}
