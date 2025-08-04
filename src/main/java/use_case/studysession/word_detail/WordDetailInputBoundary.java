package use_case.studysession.word_detail;

public interface WordDetailInputBoundary {
    /**
     * Executes the word detail retrieval process.
     *
     * @param inputData the input data containing details for the word to process
     */
    void execute(WordDetailInputData inputData);

    /**
     * Switches the view to the logged-in state.
     */
    void switchTologgedView();

    /**
     * Switches the view to the study session state.
     */
    void switchToStudySessionView();
}
