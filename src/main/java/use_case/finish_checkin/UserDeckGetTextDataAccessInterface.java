package use_case.finish_checkin;

import java.util.List;

import entity.Card;
import entity.CommonCard;

public interface UserDeckGetTextDataAccessInterface {
    /**
     * Returns the list of {@link CommonCard} instances representing the user’s word deck.
     *
     * @return an unmodifiable {@link java.util.List} of {@link CommonCard}; never {@code null}
     */
    List<Card> getWordDeck();
}
