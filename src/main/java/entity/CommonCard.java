package entity;

import java.util.Objects;
import java.util.UUID;

public final class CommonCard implements Card {

    private final UUID wordId;
    private final String text;
    private final String translation;
    private final String example;

    public CommonCard(UUID wordId, String text, String translation, String example) {
        this.wordId = Objects.requireNonNull(wordId);
        this.text = Objects.requireNonNull(text);
        this.translation = Objects.requireNonNull(translation);
        this.example = Objects.requireNonNull(example);
    }

    @Override
    public UUID getWordId() {
        return wordId;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getTranslation() {
        return translation;
    }

    @Override
    public String getExample() {
        return example;
    }
}

