package use_case.studysession.word_detail;

public interface WordDetailOutputBoundary {
    /**
     * Prepares the view for a successful retrieval of word details.
     *
     * @param out the output data containing word details to display
     */
    void prepareSuccessView(WordDetailOutputData out);

    /**
     * Switches the view to the study session state.
     */
    void switchToStudySessionView();
}
