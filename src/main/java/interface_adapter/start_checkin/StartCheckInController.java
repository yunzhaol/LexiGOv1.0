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

    /**
     * Presenter port, used here only for validation‑error feedback.
     * (The same presenter instance is also inside the interactor.)
     */
    private final StartCheckInOutputBoundary presenter;

    public StartCheckInController(StartCheckInInputBoundary interactor,
                                  StartCheckInOutputBoundary presenter) {
        this.interactor = interactor;
        this.presenter  = presenter;
    }

    /**
     * Entry point called by UI / REST layer.
     *
     * @param username user name
     * @param length   string entered in UI, should be a positive integer
     */
    public void execute(String username, String length) {

        int requestedLength;

        // range validation (Controller's job)
        try {
            // This is format issue not business logic so put here instead of interactor
            requestedLength = Integer.parseInt(length);
            if (requestedLength <= 0) {
                presenter.prepareFailView("Length must be a positive integer.");
                return;
            }
        } catch (NumberFormatException ex) {
            presenter.prepareFailView("Length must be a positive integer.");
            return;
        }


        StartCheckInInputData inputData =
                new StartCheckInInputData(username, length);

        interactor.execute(inputData);
    }
}
