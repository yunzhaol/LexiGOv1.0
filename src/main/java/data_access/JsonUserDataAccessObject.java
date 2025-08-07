package data_access;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import entity.CommonUser;
import entity.SecurityUser;
import entity.User;
import use_case.change_password.ChangePasswordUserTypeDataAccessInterface;
import use_case.change_password.make_password_change.UserPasswordDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

/**
 * Single‑file JSON storage for all user objects.
 */
public final class JsonUserDataAccessObject implements
        SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        ChangePasswordUserTypeDataAccessInterface,
        UserPasswordDataAccessInterface {

    private static final String DEFAULT_FILE_PATH = "resources//data//users.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String SECURITY = "SECURITY";
    private static final String COMMON = "COMMON";
    private File store;

    private final Map<String, User> users = new HashMap<>();
    private String currentUsername;

    public JsonUserDataAccessObject(String filePath) throws IOException {
        store = new File(filePath);
        final File parent = store.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            throw new IOException("Cannot create data dir: " + parent);
        }
        if (store.exists()) {
            load();
        }
        else if (!store.createNewFile()) {
            throw new IOException("Cannot create " + filePath);
        }
    }

    public JsonUserDataAccessObject() throws IOException {
        this(DEFAULT_FILE_PATH);
    }

    @Override
    public String getType(String username) {
        final User u = users.get(username);
        String type = null;
        if (u != null) {
            if (u instanceof SecurityUser) {
                type = SECURITY;
            }
            else if (u instanceof CommonUser) {
                type = COMMON;
            }
        }
        return type;
    }

    @Override
    public String getSecurityQuestion(String username) {
        final User u = users.get(username);
        String answer = null;
        if (u instanceof SecurityUser) {
            answer = ((SecurityUser) u).getSecurityQuestion();
        }
        return answer;
    }

    @Override
    public void update(String username, User updatedUser) {
        if (username == null || updatedUser == null) {
            throw new IllegalArgumentException("username / user == null");
        }
        users.put(username, updatedUser);
        persist();
    }

    @Override
    public String getAnswer(String username) {
        final User u = users.get(username);
        String answer = null;
        if (u instanceof SecurityUser) {
            answer = ((SecurityUser) u).getSecurityAnswer();
        }
        return answer;
    }

    @Override
    public String getQuestion(String username) {
        final User u = users.get(username);
        String question = null;
        if (u instanceof SecurityUser) {
            question = ((SecurityUser) u).getSecurityQuestion();
        }
        return question;
    }

    @Override public boolean existsByName(String id) {
        return users.containsKey(id);
    }

    @Override public void save(User user) {
        users.put(user.getName(), user);
        persist();
    }

    @Override public User get(String username) {
        return users.get(username);
    }

    @Override public void setCurrentUsername(String name) {
        this.currentUsername = name;
    }

    @Override public String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Returns an unmodifiable view of the current in-memory user map.
     *
     * <p>The returned map is a read-only wrapper created via
     * {@link java.util.Collections#unmodifiableMap(java.util.Map)}, so callers
     * cannot alter its contents. Any subsequent modifications made by this DAO
     * (e.g.&nbsp;during {@code save} or {@code update}) will not be reflected in
     * a previously obtained snapshot.</p>
     *
     * @return an immutable map of usernames to {@link entity.User} instances
     */
    public Map<String, User> snapshot() {
        return Collections.unmodifiableMap(users);
    }

    private void load() throws IOException {
        if (store.length() != 0) {
            try (Reader r = new BufferedReader(new FileReader(store))) {
                final JsonObject root = JsonParser.parseReader(r).getAsJsonObject();
                for (Map.Entry<String, JsonElement> e : root.entrySet()) {
                    final JsonObject obj = e.getValue().getAsJsonObject();
                    final String type = obj.get("type").getAsString();
                    final User u = switch (type) {
                        case COMMON -> GSON.fromJson(obj, CommonUser.class);
                        case SECURITY -> GSON.fromJson(obj, SecurityUser.class);
                        default -> throw new IllegalStateException("Unknown user type: " + type);
                    };
                    users.put(e.getKey(), u);
                }
            }
        }
    }

    private void persist() {
        try (Writer w = new BufferedWriter(new FileWriter(store, false))) {
            final Map<String, JsonObject> toWrite = new HashMap<>();
            for (Map.Entry<String, User> e : users.entrySet()) {
                final JsonObject obj = (JsonObject) GSON.toJsonTree(e.getValue());
                String type = null;
                if (e.getValue() instanceof SecurityUser) {
                    type = SECURITY;
                }
                else {
                    type = COMMON;
                }
                obj.addProperty("type", type);
                toWrite.put(e.getKey(), obj);
            }
            final Type mapType = new TypeToken<Map<String, JsonObject>>() {

            }.getType();
            GSON.toJson(toWrite, mapType, w);
        }
        catch (IOException ioe) {
            throw new RuntimeException("Failed to write user DB", ioe);
        }
    }
}

