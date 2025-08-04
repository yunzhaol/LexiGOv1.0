package use_case.studysession;

public interface StudySessionInputBoundary {

    /**
     * Handles the request to advance to the next item inputData the study session.
     *
     * @param inputData the input data containing the current study session context
     */
    void handleNextRequest(StudySessionInputData inputData);

    /**
     * Handles the request to return to the previous item inputData the study session.
     *
     * @param inputData the input data containing the current study session context
     */
    void handlePrevRequest(StudySessionInputData inputData);
}
