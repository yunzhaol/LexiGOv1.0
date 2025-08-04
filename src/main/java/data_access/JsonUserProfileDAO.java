package data_access;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import entity.Language;
import entity.PersonalProfile;
import use_case.profile.ProfileUserDataAccessInterface;
import use_case.profile.profile_set.ProfileSetUserDataAccessInterface;
import use_case.start_checkin.UserProfileDataAccessInterface;

/**
 * JSON‑based DAO that stores **all** user profiles in a single file <b>profiles.json</b>,
 * mapping <code>username → PersonalProfile</code>.
 *
 * <p>
 * Implements:
 * <ul>
 *   <li>{@link ProfileSetUserDataAccessInterface} – save / update profile</li>
 *   <li>{@link UserProfileDataAccessInterface} – query language by username</li>
 *   <li>{@link ProfileUserDataAccessInterface} – other profile‑read needs</li>
 * </ul>
 */
public class JsonUserProfileDAO implements ProfileSetUserDataAccessInterface,
        UserProfileDataAccessInterface,
        ProfileUserDataAccessInterface {

    /** Default directory on Windows; can be overridden via ctor. */
    private static final String DEFAULT_DIR =
            "resources//data";

    private static final String FILE_NAME = "profiles.json";

    private static final Language DEFAULT_LANGUAGE = Language.FR;

    private final Path filePath;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type mapType = new TypeToken<Map<String, PersonalProfile>>() { }.getType();

    public JsonUserProfileDAO() {
        this(DEFAULT_DIR);
    }

    public JsonUserProfileDAO(String dirPath) {
        final Path dir = Paths.get(dirPath);
        ensureDirectoryExists(dir);
        this.filePath = dir.resolve(FILE_NAME);
        ensureFileExists();
    }

    @Override
    public synchronized void save(PersonalProfile personalProfile) {
        if (personalProfile == null) {
            throw new IllegalArgumentException("profile == null");
        }
        final Map<String, PersonalProfile> map = readAll();
        map.put(personalProfile.getUsername(), personalProfile);
        writeAll(map);
    }

    @Override
    public synchronized Language getLanguage(String username) {
        final Map<String, PersonalProfile> map = readAll();
        PersonalProfile p = map.get(username);

        if (p == null) {
            p = new PersonalProfile(username, DEFAULT_LANGUAGE);
            map.put(username, p);
            writeAll(map);
        }
        return p.getLanguage();
    }

    /**
     * Replaces (or inserts) the {@link PersonalProfile} for the given user with a new
     * preferred {@link Language}, then persists the entire profile map.
     *
     * <p>The operation is <strong>synchronized</strong> to ensure thread-safety when the
     * underlying JSON file is read and written.</p>
     *
     * @param username     the unique user name whose language preference is being updated;
     *                     must not be {@code null}
     * @param newLanguage  the new preferred language to associate with the user;
     *                     must not be {@code null}
     * @throws IllegalArgumentException if {@code username} or {@code newLanguage} is {@code null}
     */

    public synchronized void updateLanguage(String username, Language newLanguage) {
        if (username == null || newLanguage == null) {
            throw new IllegalArgumentException("username / language is null");
        }
        final Map<String, PersonalProfile> map = readAll();
        final PersonalProfile updated = new PersonalProfile(username, newLanguage);
        map.put(username, updated);
        writeAll(map);
    }

    private void ensureDirectoryExists(Path dir) {
        try {
            Files.createDirectories(dir);
        }
        catch (IOException exception) {
            throw new IllegalStateException("Unable to create data directory: " + dir, exception);
        }
    }

    private void ensureFileExists() {
        try {
            if (!Files.exists(filePath)) {
                Files.writeString(filePath, "{}", StandardCharsets.UTF_8);
            }
        }
        catch (IOException exception) {
            throw new IllegalStateException("Unable to create profile json: " + filePath, exception);
        }
    }

    private Map<String, PersonalProfile> readAll() {
        try {
            final String json = Files.readString(filePath, StandardCharsets.UTF_8);
            Map<String, PersonalProfile> map = gson.fromJson(json, mapType);
            if (map.isEmpty()) {
                map = new HashMap<>();
            }
            return map;
        }
        catch (IOException exception) {
            throw new RuntimeException("Failed to read profiles from " + filePath, exception);
        }
    }

    private void writeAll(Map<String, PersonalProfile> map) {
        try {
            Files.writeString(filePath, gson.toJson(map, mapType), StandardCharsets.UTF_8);
        }
        catch (IOException exception) {
            throw new RuntimeException("Failed to write profiles to " + filePath, exception);
        }
    }

}
