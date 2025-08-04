package data_access;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import use_case.start_checkin.WordDataAccessInterface;

public class WordDataAccessObject implements WordDataAccessInterface {

    private final Path filePath;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<UUID, String> dictionary;

    public WordDataAccessObject() {
        this.filePath = resolveDefaultPath();
        this.dictionary = load();
    }

    public WordDataAccessObject(String path) {
        this.filePath = Paths.get(path);
        this.dictionary = load();
    }

    private static Path resolveDefaultPath() {
        try {
            final URL resource = WordDataAccessObject.class.getClassLoader()
                    .getResource("data/words.json");
            if (resource == null) {
                throw new IllegalStateException("words.json not found in resources");
            }
            return Paths.get(resource.toURI());
        }
        catch (URISyntaxException exception) {
            throw new RuntimeException("Invalid URI for words.json", exception);
        }
    }

    @Override
    public String get(UUID wordId) {
        final String word = dictionary.get(wordId);
        if (word == null) {
            throw new IllegalArgumentException("WordId not found: " + wordId);
        }
        return word;
    }

    private Map<UUID, String> load() {
        try {
            if (Files.notExists(filePath)) {
                Files.createDirectories(filePath.getParent());
                Files.writeString(filePath, "{}");
            }
            return mapper.readValue(
                    Files.newInputStream(filePath),
                    new TypeReference<Map<UUID, String>>() {

                    });
        }
        catch (IOException exception) {
            throw new IllegalStateException("Cannot read words.json", exception);
        }
    }
}
