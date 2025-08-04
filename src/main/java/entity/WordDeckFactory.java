package entity;

import java.util.List;

public interface WordDeckFactory {

    /**
     * Constructs a {@link WordDeck} containing the provided cards.
     *
     * @param cards the list of {@link CommonCard} objects to include;
     *              the collection will be copied and must not be {@code null}
     * @return a {@code WordDeck} wrapping the supplied cards
     */
    WordDeck create(List<CommonCard> cards);
}
