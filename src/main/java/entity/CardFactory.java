package entity;
import java.util.UUID;

public interface CardFactory {
    Card create(UUID wordId, String text, String translation, String example);
}
