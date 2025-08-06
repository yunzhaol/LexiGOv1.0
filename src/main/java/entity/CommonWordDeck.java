package entity;

import java.util.List;

public class CommonWordDeck implements WordDeck {
    // {UUid: {yuanwen:mao, yiwen:cat, detail:animal}}
    private final List<Card> deck;

    public CommonWordDeck(List<Card> deck) {
        this.deck = deck;
    }

    @Override
    public List<Card> getWordDeck() {
        return deck;
    }
}
