package infrastructure;

import use_case.start_checkin.WordDetailAPI;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.UnknownHostException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Adapter that calls https://api.dictionaryapi.dev/
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
        String url = BASE + URLEncoder.encode(word, StandardCharsets.UTF_8);

        Request req = new Request.Builder()
                .url(url)
                .build();

        try (Response resp = client.newCall(req).execute()) {
            if (!resp.isSuccessful()) {
                throw new IOException("HTTP " + resp.code());
            }

            JsonNode root = mapper.readTree(resp.body().byteStream());
            JsonNode example = root.path(0)
                    .path("meanings").path(0)
                    .path("definitions").path(0)
                    .path("example");

            if (example.isTextual()) {
                return example.asText();
            }
            return "No example found.";
        }
        // internet error
        catch (UnknownHostException e) {
            return "Failed connecting to the endpoint";
        }
        // other
        catch (IOException e) {
            throw new IllegalStateException("Dictionary lookup failed", e);
        }
    }
}

