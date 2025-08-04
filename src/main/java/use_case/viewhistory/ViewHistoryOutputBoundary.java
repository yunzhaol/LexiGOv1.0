package use_case.viewhistory;

public interface ViewHistoryOutputBoundary {
    /**
     * Prepares the view with the retrieved history data.
     *
     * @param outputData the data containing history entries and summary information
     */
    void prepareSuccessView(ViewHistoryOutputData outputData);

    /**
     * Prepares the view to display an error when history retrieval fails.
     *
     * @param errorMessage a message describing why history could not be retrieved
     */
    void prepareFailView(String errorMessage);
}
