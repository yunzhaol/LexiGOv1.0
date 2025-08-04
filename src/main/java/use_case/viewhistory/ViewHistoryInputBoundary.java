package use_case.viewhistory;

public interface ViewHistoryInputBoundary {
    /**
     * Executes the view history process.
     *
     * @param inputData the data required to retrieve and display history entries
     */
    void execute(ViewHistoryInputData inputData);
}
