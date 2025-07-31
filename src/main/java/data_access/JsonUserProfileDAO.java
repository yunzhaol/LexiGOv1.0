package data_access;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import entity.Language;
import entity.PersonalProfile;
import use_case.profile.ProfileUserDataAccessInterface;
import use_case.profile.profile_set.ProfileSetUserDataAccessInterface;
import use_case.start_checkin.UserProfileDataAccessInterface;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON‑based DAO that stores **all** user profiles in a single file <b>profiles.json</b>,
 * mapping <code>username → PersonalProfile</code>.
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

    private final Path filePath;          // …/profiles.json
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type mapType = new TypeToken<Map<String, PersonalProfile>>(){}.getType();

    public JsonUserProfileDAO() {
        this(DEFAULT_DIR);
    }

    public JsonUserProfileDAO(String dirPath) {
        Path dir = Paths.get(dirPath);
        ensureDirectoryExists(dir);
        this.filePath = dir.resolve(FILE_NAME);
        ensureFileExists();
    }

    @Override
    public synchronized void save(PersonalProfile personalProfile) {
        if (personalProfile == null) throw new IllegalArgumentException("profile == null");
        Map<String, PersonalProfile> map = readAll();
        map.put(personalProfile.getUsername(), personalProfile);
        writeAll(map);
    }

    @Override
    public synchronized Language getLanguage(String username) {
        PersonalProfile p = readAll().get(username);
        return p != null ? p.getLanguage() : null;
    }

    public synchronized void updateLanguage(String username, Language newLanguage) {
        if (username == null || newLanguage == null) throw new IllegalArgumentException("username / language is null");
        Map<String, PersonalProfile> map = readAll();
        PersonalProfile updated = new PersonalProfile(username, newLanguage);
        map.put(username, updated);
        writeAll(map);
    }

    private void ensureDirectoryExists(Path dir) {
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create data directory: " + dir, e);
        }
    }

    private void ensureFileExists() {
        try {
            if (!Files.exists(filePath)) {
                Files.writeString(filePath, "{}", StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create profile json: " + filePath, e);
        }
    }

    private Map<String, PersonalProfile> readAll() {
        try {
            String json = Files.readString(filePath, StandardCharsets.UTF_8);
            Map<String, PersonalProfile> map = gson.fromJson(json, mapType);
            return map != null ? map : new HashMap<>();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read profiles from " + filePath, e);
        }
    }

    private void writeAll(Map<String, PersonalProfile> map) {
        try {
            Files.writeString(filePath, gson.toJson(map, mapType), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write profiles to " + filePath, e);
        }
    }

}
