package entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface LearnRecord {
    String getUsername();

    LocalDateTime getEndTime();

    List<UUID> getLearnedWordIds();
}
