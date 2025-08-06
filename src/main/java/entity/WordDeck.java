package entity;

import java.util.List;

public interface WordDeck {
    /**
     * Retrieves the current word deck.
     *
     * @return an unmodifiable {@link java.util.List} of {@link CommonCard} instances
     */
    List<Card> getWordDeck();
}
