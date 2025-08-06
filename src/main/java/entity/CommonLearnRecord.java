package entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class CommonLearnRecord implements LearnRecord {
    private final String username;
    private final LocalDateTime endTime;
    private final List<UUID> learnwords;

    public CommonLearnRecord(String username, LocalDateTime endTime, List<UUID> learnwords) {
        this.username = username;
        this.endTime = endTime;
        this.learnwords = learnwords;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public List<UUID> getLearnedWordIds() {
        return learnwords;
    }
}
