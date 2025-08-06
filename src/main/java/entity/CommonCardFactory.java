package entity;

import java.util.UUID;

public class CommonCardFactory implements CardFactory {

    @Override
    public Card create(UUID wordId, String text, String translation, String example) {
        return new CommonCard(wordId, text, translation, example);
    }
}
