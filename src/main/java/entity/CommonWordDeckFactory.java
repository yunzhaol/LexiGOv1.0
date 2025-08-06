package entity;

import java.util.List;

public class CommonWordDeckFactory implements WordDeckFactory {

    @Override
    public WordDeck create(List<Card> deck) {
        return new CommonWordDeck(deck);
    }
}
