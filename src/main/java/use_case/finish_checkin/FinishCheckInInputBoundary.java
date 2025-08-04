package use_case.finish_checkin;

public interface FinishCheckInInputBoundary {
    /**
     * Executes the finish-check-in process.
     *
     * @param inputData encapsulates all data required to finish the check-in; never {@code null}
     */
    void execute(FinishCheckInInputData inputData);
}
