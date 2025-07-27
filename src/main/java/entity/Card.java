package entity;
import java.util.UUID;

public interface Card {
    UUID   getWordId();
    String getText();
    String getTranslation();
    String getExample();
}

