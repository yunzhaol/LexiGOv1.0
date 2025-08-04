package use_case.start_checkin;

import java.util.UUID;

public interface WordDataAccessInterface {
    /**
     * Retrieves the text representation of a word by its unique identifier.
     *
     * @param wordId the UUID of the word to fetch
     * @return the word text corresponding to the given ID
     */
    String get(UUID wordId);
}
