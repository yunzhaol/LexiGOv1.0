package use_case.viewhistory;

import java.util.List;

import entity.LearnRecord;
import use_case.gateway.UserRecordDataAccessInterface;

/**
 * Interactor for handling the View History use case.
 */
public class ViewHistoryInteractor implements ViewHistoryInputBoundary {

    private final UserRecordDataAccessInterface dataAccess;
    private final ViewHistoryOutputBoundary presenter;
    private final ViewHistoryProcessorService processor;

    /**
     * Constructor with injectable dependencies for testing/customization.
     *
     * @param dataAccess the data access interface for retrieving user records
     * @param presenter  the output boundary for presenting results
     * @param processor  the service for processing raw history records
     */
    public ViewHistoryInteractor(UserRecordDataAccessInterface dataAccess,
                                 ViewHistoryOutputBoundary presenter,
                                 ViewHistoryProcessorService processor) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.processor = processor;
    }

    /**
     * Executes the view history workflow.
     *
     * @param inputData the input data containing the username whose history is requested
     */
    @Override
    public void execute(ViewHistoryInputData inputData) {
        final String username = inputData.getUsername();

        // Retrieve records from data layer
        final List<LearnRecord> records = dataAccess.get(username);

        // Early return if no records are found
        if (records.isEmpty()) {
            presenter.prepareFailView("No learning records found for user: " + username);
        }
        else {
            // Delegate processing of raw records
            final List<ViewHistoryEntryData> processedSessions = processor.processRecords(records);

            // Compute aggregates
            final int totalSessions = processedSessions.size();
            final int totalWords = calculateTotalWords(processedSessions);

            // Build and present the output data
            final ViewHistoryOutputData result = new ViewHistoryOutputData(
                    username,
                    processedSessions,
                    totalSessions,
                    totalWords
            );
            presenter.prepareSuccessView(result);
        }
    }

    /**
     * Calculates the total number of words across all processed sessions.
     *
     * @param sessions the list of processed history entries
     * @return the sum of wordsCount from each session
     */
    private int calculateTotalWords(List<ViewHistoryEntryData> sessions) {
        return sessions.stream()
                .mapToInt(ViewHistoryEntryData::getWordsCount)
                .sum();
    }
}

