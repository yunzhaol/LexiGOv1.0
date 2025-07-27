package data_access;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import use_case.start_checkin.WordDataAccessInterface;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

/**
 * Read a JSON file like:
 * {
 *   "550e8400-e29b-41d4-a716-446655440000": "apple",
 *   "3fa85f64-5717-4562-b3fc-2c963f66afa6": "table"
 * }
 * and expose word text lookup by UUID.
 */
public class WordDataAccessObject implements WordDataAccessInterface {

    private static Path FILE;

    static {
        try {
            URL resource = WordBookDataAccessObject.class.getClassLoader()
                    .getResource("data/words.json");
            if (resource == null) {
                throw new IllegalStateException("words.json not found in resources");
            }
            FILE = Paths.get(resource.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private final ObjectMapper mapper = new ObjectMapper();

    /** cached whole dictionary: UUID → word */
    private final Map<UUID, String> dictionary;

    public WordDataAccessObject() {
        this.dictionary = load();
    }

    @Override
    public String get(UUID wordId) {
        String word = dictionary.get(wordId);
        if (word == null) {
            throw new IllegalArgumentException("WordId not found: " + wordId);
        }
        return word;
    }

    private Map<UUID, String> load() {
        try {
            if (Files.notExists(FILE)) {
                Files.createDirectories(FILE.getParent());
                Files.writeString(FILE, "{}");
            }
            return mapper.readValue(
                    Files.newInputStream(FILE),
                    new TypeReference<Map<UUID, String>>() {});
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read words.json", e);
        }
    }
}
