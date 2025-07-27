package data_access;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.CommonWordBook;
import entity.WordBook;
import use_case.start_checkin.WordBookAccessInterface;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Read a local JSON file like:
 * {
 *   "TOEIC-800": ["550e8400-e29b-41d4-a716-446655440000", ...],
 *   "GRE-High":  ["3fa85f64-5717-4562-b3fc-2c963f66afa6"]
 * }
 * and convert it to a CommonWordBook.
 */
public class WordBookDataAccessObject implements WordBookAccessInterface {
    private static Path FILE;
    static {
        try {
            URL resource = WordBookDataAccessObject.class
                    .getClassLoader()
                    .getResource("data/wordbook.json");

            if (resource == null) {
                throw new IllegalStateException("wordbook.json not found in resources");
            }

            FILE = Paths.get(resource.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private final ObjectMapper mapper = new ObjectMapper();

    /** simple in‑memory cache */
    private Map<String, List<UUID>> cache;

    public WordBookDataAccessObject() {
        this.cache = load();
    }

    @Override
    public WordBook get() {
        if (cache.isEmpty())
            throw new IllegalStateException("No word book found in JSON file");
        // choose the first entry as default; or inject a book name in the ctor
        Map.Entry<String, List<UUID>> entry = cache.entrySet().iterator().next();
        return new CommonWordBook(entry.getKey(), entry.getValue());
    }

    private Map<String, List<UUID>> load() {
        try {
            if (Files.notExists(FILE)) {
                Files.createDirectories(FILE.getParent());
                Files.writeString(FILE, "{}");
            }
            return mapper.readValue(Files.newInputStream(FILE),
                    new TypeReference<Map<String, List<UUID>>>() {});
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read wordbook.json", e);
        }
    }
}
