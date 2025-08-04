package use_case.studysession;

public interface UserDeckgetterDataAccessInterface {
    /**
     * Retrieves the text entry at the specified index from the user's deck.
     *
     * @param index the index of the text entry to retrieve
     * @return the text entry corresponding to the specified index
     */
    String getText(int index);
}
