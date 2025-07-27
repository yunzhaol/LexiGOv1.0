package infrastructure;

import entity.Language;
import use_case.start_checkin.WordTranslationAPI;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * Adapter around the <a href="https://www.deepl.com/docs-api">DeepL v2 REST API</a>.
 *
 * <p>this demo targets the <b>FREE</b> endpoint
 * (<code>https://api-free.deepl.com/v2/translate</code>).
 * If you are on a paid Pro plan you <i>must</i> change the base URL to
 * <code>https://api.deepl.com/v2/translate</code>.</p>
 *
 * <p>The implementation keeps dependencies to zero (only JDK HTTP/JSON)
 * so it can be pasted into any plain‑vanilla Java project. Feel free
 * to replace the ad‑hoc JSON parsing with Jackson/Gson/etc.</p>
 */
public class DeepLAPIAdapter implements WordTranslationAPI {


    /** Free‑tier base URL (see javadoc above for the Pro URL). */
    private static final String ENDPOINT = "https://api-free.deepl.com/v2/translate";

    /**
     * API key – <b>never</b> commit real keys to source control.
     * <p>Here we fall back to an environment variable so you can
     * keep secrets outside the repo:</p>
     *
     * <pre>export DEEPL_AUTH_KEY=xxxxxxxxxxxxxxxx</pre>
     */
    private static final String AUTH_KEY =
            System.getenv().getOrDefault("DEEPL_AUTH_KEY",
                    "c9026365-468c-48cf-a7cd-be2f95d40a58:fx");

    /** Thread‑safe, shareable HTTP client. */
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    /**
     * Translate plain text using DeepL.
     *
     * @param text       UTF‑8 text to be translated (must not be {@code null/blank})
     * @param targetLang target language enum (must not be {@code null})
     * @return translated string; empty when input invalid or parsing fails
     * @throws RuntimeException wrapping network / API errors
     */
    @Override
    public String getTranslation(String text, Language targetLang) {
        /* 1  Guard‑clauses for null/empty arguments */
        if (text == null || text.isBlank() || targetLang == null) {
            return "";
        }

        /* 2️  Build the x‑www‑form‑urlencoded body
         *     - Multiple text parameters are allowed; we send one for simplicity.
         *     - target_lang MUST be uppercase per the official spec.
         */
        String form = "text=" + urlEncode(text) +
                "&target_lang=" + targetLang.code().toUpperCase();

        /* 3️  Assemble the POST request */
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("Authorization", "DeepL-Auth-Key " + AUTH_KEY)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();

        /* 4️  Fire the request and handle the response */
        try {
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());

            /* DeepL returns JSON even for errors; only 200 is “all good”. */
            if (response.statusCode() != 200) {
                // Common errors: 456 (quota exceeded), 429 (too many requests)
                throw new RuntimeException("DeepL returned HTTP "
                        + response.statusCode() + " – " + response.body());
            }
            return parseTranslatedText(response.body());

        } catch (IOException | InterruptedException e) {
            /* Preserve the interrupt status if we were interrupted */
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException("DeepL request failed", e);
        }
    }


    /** URL‑encodes a string for form submission (UTF‑8). */
    private static String urlEncode(String raw) {
        return URLEncoder.encode(raw, StandardCharsets.UTF_8);
    }

    /**
     * Ultra‑lightweight JSON parser that yanks the first <code>"text":"…"</code> value.
     * <p>
     * Expected shape (simplified):
     * <pre>{
     *   "translations":[
     *     {"detected_source_language":"EN","text":"你好，世界！"}
     *   ]
     * }</pre>
     * <p>
     * Robust production code should delegate to a proper JSON library.
     */
    private static String parseTranslatedText(String json) {
        final String MARKER = "\"text\":\"";
        int start = json.indexOf(MARKER);
        if (start < 0) return "";
        start += MARKER.length();
        int end = json.indexOf('"', start);
        return end > start ? json.substring(start, end) : "";
    }
}
