package use_case.studysession;

public interface StudySessionOutputBoundary {
    /**
     * Prepares the view to display successful study session results.
     *
     * @param out the output data containing the results of the successful study session
     */
    void prepareSuccessView(StudySessionOutputData out);

    /**
     * Prepares the view to display failed study session results.
     *
     * @param out the output data containing the details of the failed study session
     */
    void prepareFailureView(StudySessionOutputData out);
}
