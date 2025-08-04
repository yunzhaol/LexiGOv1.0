package use_case.finish_checkin;

public interface FinishCheckInOutputBoundary {
    /**
     * Presents a successful check-in completion event to the view layer.
     */
    void prepareSuccessView();
}
