package entity;

import java.util.List;
import java.util.UUID;

public class CommonWordBookFactory implements WordBookFactory {

    @Override
    public WordBook create(String name, List<UUID> initialWordIDs) {
        return new CommonWordBook(name, initialWordIDs);
    }
}

