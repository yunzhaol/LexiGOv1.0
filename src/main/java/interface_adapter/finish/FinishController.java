package interface_adapter.finish;

import use_case.finish_checkin.FinishCheckInInputBoundary;
import use_case.finish_checkin.FinishCheckInInputData;

public class FinishController {
    private final FinishCheckInInputBoundary interactor;

    public FinishController(FinishCheckInInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Triggers the finish-check-in use case for the specified user.
     *
     * @param username the user who has just completed their check-in; must not be {@code null}
     */
    public void execute(String username) {
        interactor.execute(new FinishCheckInInputData(username));
    }
}
