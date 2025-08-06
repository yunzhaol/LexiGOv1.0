package use_case.word_detail;

import data_access.WordDataAccessObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WordDataAccessObjectTest {

    /**
     * Verifies that the no-arg constructor (which resolves
     * the default classpath resource) does not throw.
     */
    @Test
    void defaultConstructorLoadsWithoutException() {
        assertDoesNotThrow(() -> new WordDataAccessObject());
    }

    /**
     * When the given path does not exist, load() should
     * create its parent dirs and write "{}" to the file.
     * Afterwards, get(...) on any UUID must throw IllegalArgumentException.
     */
    @Test
    void customPathCreatesEmptyJsonWhenNotExists(@TempDir Path tempDir) throws IOException {
        Path jsonFile = tempDir.resolve("nested").resolve("words.json");
        assertFalse(Files.exists(jsonFile), "Precondition: file should not exist before constructor");

        var dao = new WordDataAccessObject(jsonFile.toString());

        // file and parent directory must now exist
        assertTrue(Files.exists(jsonFile), "JSON file should have been created");
        assertEquals("{}", Files.readString(jsonFile), "Newly created file should contain an empty JSON object");

        // get on a random UUID should throw with a clear message
        UUID randomId = UUID.randomUUID();
        var ex = assertThrows(IllegalArgumentException.class, () -> dao.get(randomId));
        assertTrue(ex.getMessage().contains("WordId not found"), "Exception must mention missing WordId");
    }

    /**
     * When the JSON file already exists and contains a mapping,
     * get(...) should return the correct value.
     */
    @Test
    void customPathLoadsExistingJson(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("words.json");
        Files.createDirectories(file.getParent());

        String id = "123e4567-e89b-12d3-a456-426655440000";
        String json = "{\"" + id + "\":\"hello\"}";
        Files.writeString(file, json);

        var dao = new WordDataAccessObject(file.toString());
        assertEquals("hello", dao.get(UUID.fromString(id)), "Should read back 'hello' for the given UUID");
    }

    /**
     * Even if the file exists but is empty (i.e. "{}"), get on
     * any unknown key should include the UUID string in the message.
     */
    @Test
    void getWithUnknownIdThrowsException(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("words.json");
        Files.createDirectories(file.getParent());
        Files.writeString(file, "{}");

        var dao = new WordDataAccessObject(file.toString());
        UUID unknown = UUID.randomUUID();

        var ex = assertThrows(IllegalArgumentException.class, () -> dao.get(unknown));
        assertTrue(ex.getMessage().contains(unknown.toString()),
                "Exception message should include the missing UUID");
    }

    /**
     * If the path given is actually a directory, load()'s
     * attempt to open it as a file will cause an IOException,
     * which should be wrapped in IllegalStateException.
     */
    @Test
    void loadThrowsWhenFileIsDirectory(@TempDir Path tempDir) {
        // pass the tempDir itself (a directory) as the 'path' argument
        var ex = assertThrows(IllegalStateException.class,
                () -> new WordDataAccessObject(tempDir.toString()));
        assertTrue(ex.getMessage().contains("Cannot read words.json"),
                "Must wrap IO errors in IllegalStateException with the correct message");
    }
}

