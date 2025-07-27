package entity;

import java.util.List;


public class CommonWordDeck implements WordDeck {
    // {UUid: {yuanwen:mao, yiwen:cat, detail:animal}}
    private final List<CommonCard> deck;

    public CommonWordDeck(List<CommonCard> deck) {
        this.deck = deck;
    }

    @Override
    public List<CommonCard> getWordDeck() {
        return deck;
    }
}
