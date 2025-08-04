package interface_adapter.view_history;

import use_case.viewhistory.ViewHistoryOutputBoundary;
import use_case.viewhistory.ViewHistoryOutputData;

/**
 * ViewHistory Presenter.
 */
public class ViewHistoryPresenter implements ViewHistoryOutputBoundary {

    private final ViewHistoryViewModel viewModel;

    public ViewHistoryPresenter(ViewHistoryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ViewHistoryOutputData outputData) {
        final interface_adapter.view_history.ViewHistoryState state = viewModel.getState();

        state.setCurrentUser(outputData.getUsername());
        state.setSessions(outputData.getSessions());
        state.setTotalSessions(outputData.getTotalSessions());
        state.setTotalWords(outputData.getTotalWords());
        state.setErrorMessage(null);

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final interface_adapter.view_history.ViewHistoryState state = viewModel.getState();

        state.setErrorMessage(errorMessage);
        state.setSessions(null);
        state.setTotalSessions(0);
        state.setTotalWords(0);

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
