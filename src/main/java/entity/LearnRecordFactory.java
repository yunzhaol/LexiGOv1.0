package entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface LearnRecordFactory {
    /**
     * Creates a {@link LearnRecord} describing one finished learning session.
     *
     * @param username   the username; must not be {@code null}
     * @param endTime    the timestamp at which the session ended; must not be {@code null}
     * @param learnwords the list of word IDs learned in this session;
     *                   must not be {@code null} (maybe empty)
     * @return a fully initialised {@link LearnRecord}
     */
    LearnRecord create(String username, LocalDateTime endTime, List<UUID> learnwords);
}

