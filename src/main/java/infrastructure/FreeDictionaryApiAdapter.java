package infrastructure;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import use_case.start_checkin.WordDetailAPI;

/**
 * Adapter that calls https://api.dictionaryapi.dev/.
 * and extracts the first example sentence, e.g.:
 *   GET /api/v2/entries/en/hello  →  "hello there, Katie!"
 */
public class FreeDictionaryApiAdapter implements WordDetailAPI {

    private static final String BASE =
            "https://api.dictionaryapi.dev/api/v2/entries/en/";

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String getWordExample(String word) {
        final String url = BASE + URLEncoder.encode(word, StandardCharsets.UTF_8);

        final Request req = new Request.Builder()
                .url(url)
                .build();
        String response;
        try (Response resp = client.newCall(req).execute()) {
            if (!resp.isSuccessful()) {
                throw new IOException("HTTP " + resp.code());
            }

            final JsonNode root = mapper.readTree(resp.body().byteStream());
            final JsonNode example = root.path(0)
                    .path("meanings").path(0)
                    .path("definitions").path(0)
                    .path("example");

            if (example.isTextual()) {
                response = example.asText();
            }
            else {
                response = "No example found.";
            }
        }
        // internet error
        catch (UnknownHostException exception) {
            response = "Failed connecting to the endpoint";
        }
        // other
        catch (IOException exception) {
            throw new IllegalStateException("Dictionary lookup failed", exception);
        }
        return response;
    }
}

