package data_access;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import entity.CommonLearnRecord;          // ← 具体实现类
import entity.LearnRecord;
import use_case.finish_checkin.UserSaveRecordDataAccessInterface;
import use_case.rank.RankUserDataAccessInterface;
import use_case.gateway.UserRecordDataAccessInterface;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class JsonUserRecordDataAccessObject
        implements UserRecordDataAccessInterface, UserSaveRecordDataAccessInterface, RankUserDataAccessInterface {

    /* -------- 默认文件位置 -------- */
    private static final Path DEFAULT_PATH = Paths.get("resources", "data", "learn_record.json");

    /* -------- Gson 配置 -------- */
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Gson gson;

    private final Type mapType = new TypeToken<Map<String, List<CommonLearnRecord>>>() {}.getType();

    private final Map<String, List<CommonLearnRecord>> cache;

    private final Path store;

    public JsonUserRecordDataAccessObject() {
        this(DEFAULT_PATH);
    }

    public JsonUserRecordDataAccessObject(String filePath) {
        this(Paths.get(filePath));
    }

    JsonUserRecordDataAccessObject(Path filePath) {
        this.store = filePath == null ? DEFAULT_PATH : filePath;

        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                        (com.google.gson.JsonSerializer<LocalDateTime>) (src, t, ctx) -> ctx.serialize(src.format(ISO)))
                .registerTypeAdapter(LocalDateTime.class,
                        (com.google.gson.JsonDeserializer<LocalDateTime>) (json, t, ctx) -> LocalDateTime.parse(json.getAsString(), ISO))
                .setPrettyPrinting()
                .create();

        this.cache = loadFromDisk();
    }

    @Override
    public synchronized List<LearnRecord> get(String username) {
        return new ArrayList<>(cache.getOrDefault(username, List.of()));
    }

    @Override
    public synchronized Map<String, List<LearnRecord>> getAllUsers() {
        Map<String, List<LearnRecord>> result = new HashMap<>();
        cache.forEach((user, list) -> result.put(user, new ArrayList<>(list)));
        return Collections.unmodifiableMap(result);
    }

    @Override
    public synchronized void save(LearnRecord rec) {
        Objects.requireNonNull(rec.getUsername(), "username must not be null");
        CommonLearnRecord record = (CommonLearnRecord) rec;
        cache.computeIfAbsent(record.getUsername(), k -> new ArrayList<>()).add(record);
        flush();
    }

    private Map<String, List<CommonLearnRecord>> loadFromDisk() {
        try {
            if (Files.notExists(store)) {
                Files.createDirectories(store.getParent());
                Files.writeString(store, "{}", StandardCharsets.UTF_8);
            }
            String json = Files.readString(store);
            Map<String, List<CommonLearnRecord>> map = gson.fromJson(json, mapType);
            return map != null ? map : new HashMap<>();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read " + store, e);
        }
    }

    private void flush() {
        try {
            Files.writeString(store, gson.toJson(cache, mapType));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write " + store, e);
        }
    }
}
