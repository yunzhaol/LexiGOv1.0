package interface_adapter.view_history;

import use_case.viewhistory.ViewHistoryInputBoundary;
import use_case.viewhistory.ViewHistoryInputData;

public class ViewHistoryController {

    private final ViewHistoryInputBoundary viewHistoryInteractor;

    public ViewHistoryController(ViewHistoryInputBoundary viewHistoryInteractor) {
        this.viewHistoryInteractor = viewHistoryInteractor;
    }

    /**
     * Executes the view-history use case for the specified user.
     *
     * @param username the user whose history is requested; must not be {@code null}
     */
    public void execute(String username) {
        final ViewHistoryInputData inputData = new ViewHistoryInputData(username);
        viewHistoryInteractor.execute(inputData);
    }
}
