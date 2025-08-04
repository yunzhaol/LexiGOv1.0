package entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface LearnRecord {
    /**
     * Returns the username associated with this learning record.
     *
     * @return the user name; never {@code null}
     */
    String getUsername();

    /**
     * Returns the timestamp at which the learning session finished.
     *
     * @return the end time; never {@code null}
     */
    LocalDateTime getEndTime();

    /**
     * Returns an immutable list of word identifiers learned in this session.
     *
     * @return an unmodifiable {@link java.util.List} of {@link java.util.UUID}s
     */
    List<UUID> getLearnedWordIds();

}
