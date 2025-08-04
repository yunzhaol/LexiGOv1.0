package use_case.start_checkin;

import entity.WordBook;

public interface WordBookAccessInterface {
    /**
     * Retrieves the current WordBook.
     *
     * @return the WordBook instance
     */
    WordBook get();
}
