package entity;

import java.util.List;
import java.util.UUID;

public class CommonWordBook implements WordBook{

    private final List<UUID> words;
    private String name;

    public CommonWordBook(String name, List<UUID> words) {
        this.words = words;
        this.name = name;
    }

    @Override
    public List<UUID> getWordIds() {
        return words;
    }

    public String getName() {
        return name;
    }
}
