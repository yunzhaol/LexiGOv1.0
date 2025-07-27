package entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface LearnRecordFactory {

    LearnRecord create(String username, LocalDateTime endTime, List<UUID> learnwords);
}

