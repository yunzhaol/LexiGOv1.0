package use_case.viewhistory;


import entity.LearnRecord;
import use_case.gateway.UserRecordDataAccessInterface;

import java.util.List;

/**
 * ViewHistory Interactor - Clean and focused implementation.
 * Delegates complex processing to DefaultViewHistoryProcessorService.
 */
public class ViewHistoryInteractor implements ViewHistoryInputBoundary {

    private final UserRecordDataAccessInterface dataAccess;
    private final ViewHistoryOutputBoundary presenter;
    private final ViewHistoryProcessorService processor;

    /**
     * Default constructor using standard processing.
     */
    public ViewHistoryInteractor(UserRecordDataAccessInterface dataAccess,
                                 ViewHistoryOutputBoundary presenter) {
        this(dataAccess, presenter, new DefaultViewHistoryProcessorService());
    }

    /**
     * Constructor with injectable processor for testing/customization.
     */
    public ViewHistoryInteractor(UserRecordDataAccessInterface dataAccess,
                                 ViewHistoryOutputBoundary presenter,
                                 ViewHistoryProcessorService processor) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.processor = processor;
    }

    @Override
    public void execute(ViewHistoryInputData inputData) {
        String username = inputData.getUsername();

        // Get records from data layer
        List<LearnRecord> records = dataAccess.get(username);

        // Early return for empty results
        if (records.isEmpty()) {
            presenter.prepareFailView("No learning records found for user: " + username);
            return;
        }

        // Delegate all complex processing to service
        List<ViewHistoryEntryData> processedSessions = processor.processRecords(records);

        // Calculate simple aggregates
        int totalSessions = processedSessions.size();
        int totalWords = calculateTotalWords(processedSessions);

        // Create and present result
        ViewHistoryOutputData result = new ViewHistoryOutputData(
                username, processedSessions, totalSessions, totalWords);

        presenter.prepareSuccessView(result);
    }

    /**
     * Simple helper method for calculating total words.
     */
    private int calculateTotalWords(List<ViewHistoryEntryData> sessions) {
        return sessions.stream()
                .mapToInt(ViewHistoryEntryData::getWordsCount)
                .sum();
    }
}