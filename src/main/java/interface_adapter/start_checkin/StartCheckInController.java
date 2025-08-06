package interface_adapter.start_checkin;

import use_case.start_checkin.StartCheckInInputBoundary;
import use_case.start_checkin.StartCheckInInputData;
import use_case.start_checkin.StartCheckInOutputBoundary;

/**
 * Controller layer: translates raw request parameters
 * into a clean StartCheckInInputData DTO and calls the use‑case.
 */
public class StartCheckInController {

    /**
     * Use‑case port (Interactor implements this).
     */
    private final StartCheckInInputBoundary interactor;

    public StartCheckInController(StartCheckInInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Entry point called by UI / REST layer.
     *
     * @param username user name
     * @param length   string entered in UI, should be a positive integer
     */
    public void execute(String username, String length) {

        final StartCheckInInputData inputData =
                    new StartCheckInInputData(username, length);

        interactor.execute(inputData);
    }
}

