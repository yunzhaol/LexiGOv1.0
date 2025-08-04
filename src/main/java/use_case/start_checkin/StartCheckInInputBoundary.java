package use_case.start_checkin;

public interface StartCheckInInputBoundary {
    /**
     * Executes the start check-in process.
     *
     * @param inputData the data required to initiate check-in
     */
    void execute(StartCheckInInputData inputData);
}
