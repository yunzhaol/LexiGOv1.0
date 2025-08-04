package use_case.start_checkin;

public interface StartCheckInOutputBoundary {
    /**
     * Prepares the view for a successful check-in.
     *
     * @param outputData the data to display on success
     */
    void prepareSuccessView(StartCheckInOutputData outputData);

    /**
     * Prepares the view for a failed check-in.
     *
     * @param errorMessage message describing why the check-in failed
     */
    void prepareFailView(String errorMessage);
}
