package entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CommonLearnRecordFactory implements LearnRecordFactory {

    @Override
    public LearnRecord create(String username, LocalDateTime endTime, List<UUID> learnwords) {
        return new CommonLearnRecord(username, endTime, learnwords);
    }
}
