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

/**
 * Persist user learning records in JSON using Gson.
 * File location: E:\LexiGOv1.1\data\learn_record.json
 */
public class JsonUserRecordDataAccessObject
        implements UserRecordDataAccessInterface, UserSaveRecordDataAccessInterface, RankUserDataAccessInterface {

    /* -------- 文件位置 -------- */
    private static final Path path = Paths.get("resources", "data", "learn_record.json");

    /* -------- Gson 配置 -------- */
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Gson gson;

    /** Map<username, List<CommonLearnRecord>> */
    private final Type mapType =
            new TypeToken<Map<String, List<CommonLearnRecord>>>() {}.getType();

    private final Map<String, List<CommonLearnRecord>> cache;

    public JsonUserRecordDataAccessObject() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                        (com.google.gson.JsonSerializer<LocalDateTime>)
                                (src, t, ctx) -> ctx.serialize(src.format(ISO)))
                .registerTypeAdapter(LocalDateTime.class,
                        (com.google.gson.JsonDeserializer<LocalDateTime>)
                                (json, t, ctx) -> LocalDateTime.parse(json.getAsString(), ISO))
                .setPrettyPrinting()
                .create();

        cache = loadFromDisk();
    }

    @Override
    public synchronized List<LearnRecord> get(String username) {
        return new ArrayList<>(cache.getOrDefault(username, List.of()));
    }

    @Override
    public synchronized Map<String, List<LearnRecord>> getAllUsers() {
        Map<String, List<LearnRecord>> result = new HashMap<>();

        cache.forEach((user, list) -> {

            result.put(user, new ArrayList<>(list));
        });

        return Collections.unmodifiableMap(result);
    }

    @Override
    public synchronized void save(LearnRecord rec) {
        Objects.requireNonNull(rec.getUsername(), "username must not be null");

        CommonLearnRecord record = (CommonLearnRecord) rec;

        cache.computeIfAbsent(record.getUsername(), k -> new ArrayList<>())
                .add(record);
        flush();
    }

    private Map<String, List<CommonLearnRecord>> loadFromDisk() {
        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path.getParent());
                Files.writeString(path, "{}", StandardCharsets.UTF_8);
            }

            String json = Files.readString(path);
            Map<String, List<CommonLearnRecord>> map =
                    gson.fromJson(json, mapType);
            return map != null ? map : new HashMap<>();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read learn_record.json", e);
        }
    }

    private void flush() {
        try {
            Files.writeString(path, gson.toJson(cache, mapType));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write learn_record.json", e);
        }
    }

}
