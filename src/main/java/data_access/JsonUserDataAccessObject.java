package data_access;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import entity.CommonUser;
import entity.SecurityUser;
import entity.User;
import use_case.change_password.ChangePasswordUserTypeDataAccessInterface;
import use_case.change_password.make_password_change.UserPasswordDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    private static final Gson   GSON      = new GsonBuilder().setPrettyPrinting().create();
    private File store;

    private final Map<String, User>       users  = new HashMap<>();
    private       String                  currentUsername;

    public JsonUserDataAccessObject(String filePath) throws IOException {
        store = new File(filePath);
        File parent = store.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            throw new IOException("Cannot create data dir: " + parent);
        }
        if (store.exists()) {
            load();
        } else if (!store.createNewFile()) {
            throw new IOException("Cannot create " + filePath);
        }
    }

    public JsonUserDataAccessObject() throws IOException {
        this(DEFAULT_FILE_PATH);
    }

    @Override
    public String getType(String username) {
        User u = users.get(username);
        if (u == null) return null;
        return (u instanceof SecurityUser) ? "SECURITY" : "COMMON";
    }

    @Override
    public String getSecurityQuestion(String username) {
        User u = users.get(username);
        if (u instanceof SecurityUser) {
            return ((SecurityUser) u).getSecurityQuestion();
        }
        return null;
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
        User u = users.get(username);
        if (u instanceof SecurityUser) {
            return ((SecurityUser) u).getSecurityAnswer();
        }
        return null;
    }

    @Override
    public String getQuestion(String username) {
        User u = users.get(username);
        if (u instanceof SecurityUser) {
            return ((SecurityUser) u).getSecurityQuestion();
        }
        return null;
    }

    @Override public boolean existsByName(String id)         { return users.containsKey(id); }
    @Override public void    save(User user)                 { users.put(user.getName(), user); persist(); }
    @Override public User    get(String username)            { return users.get(username); }
    @Override public void    setCurrentUsername(String name) { this.currentUsername = name; }
    @Override public String  getCurrentUsername()            { return currentUsername; }

    public Map<String, User> snapshot() { return Collections.unmodifiableMap(users); }

    private void load() throws IOException {
        if (store.length() == 0) return;
        try (Reader r = new BufferedReader(new FileReader(store))) {
            JsonObject root = JsonParser.parseReader(r).getAsJsonObject();
            for (Map.Entry<String, JsonElement> e : root.entrySet()) {
                JsonObject obj  = e.getValue().getAsJsonObject();
                String     type = obj.get("type").getAsString();
                User u = switch (type) {
                    case "COMMON"   -> GSON.fromJson(obj, CommonUser.class);
                    case "SECURITY" -> GSON.fromJson(obj, SecurityUser.class);
                    default         -> throw new IllegalStateException("Unknown user type: " + type);
                };
                users.put(e.getKey(), u);
            }
        }
    }

    private void persist() {
        try (Writer w = new BufferedWriter(new FileWriter(store, false))) {
            Map<String, JsonObject> toWrite = new HashMap<>();
            for (Map.Entry<String, User> e : users.entrySet()) {
                JsonObject obj = (JsonObject) GSON.toJsonTree(e.getValue());
                obj.addProperty("type", (e.getValue() instanceof SecurityUser) ? "SECURITY" : "COMMON");
                toWrite.put(e.getKey(), obj);
            }
            Type mapType = new TypeToken<Map<String, JsonObject>>() {}.getType();
            GSON.toJson(toWrite, mapType, w);
        } catch (IOException ioe) {
            throw new RuntimeException("Failed to write user DB", ioe);
        }
    }
}

