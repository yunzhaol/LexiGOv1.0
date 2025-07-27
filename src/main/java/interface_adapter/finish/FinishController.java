package interface_adapter.finish;

import use_case.finish_checkin.FinishCheckInInputBoundary;
import use_case.finish_checkin.FinishCheckInInputData;

public class FinishController {
    private final FinishCheckInInputBoundary interactor;

    public FinishController(FinishCheckInInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String username) {
        interactor.execute(new FinishCheckInInputData(username));
    }
}
