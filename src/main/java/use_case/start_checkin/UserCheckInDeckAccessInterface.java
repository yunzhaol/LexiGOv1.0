package use_case.start_checkin;

import entity.WordDeck;

public interface UserCheckInDeckAccessInterface {
    /**
     * Persists the provided WordDeck for a user's check-in session.
     *
     * @param deck the WordDeck instance to save
     */
    void save(WordDeck deck);
}
