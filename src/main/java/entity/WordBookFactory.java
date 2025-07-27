package entity;

import java.util.List;
import java.util.UUID;

public interface WordBookFactory {
    WordBook create(String name, List<UUID> initialWordIDs);
}
