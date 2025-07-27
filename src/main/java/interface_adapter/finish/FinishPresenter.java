package interface_adapter.finish;

import interface_adapter.ViewManagerModel;
import interface_adapter.session.LoggedInViewModel;
import interface_adapter.studysession.StudySessionState;
import interface_adapter.studysession.StudySessionViewModel;
import use_case.finish_checkin.FinishCheckInOutputBoundary;

public class FinishPresenter implements FinishCheckInOutputBoundary {
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;
    private final StudySessionViewModel studySessionViewModel;

    public FinishPresenter(LoggedInViewModel loggedInViewModel,
                           ViewManagerModel viewManagerModel,
                           StudySessionViewModel studySessionViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;
        this.studySessionViewModel = studySessionViewModel;
    }

    @Override
    public void prepareSuccessView() {
        StudySessionState state = studySessionViewModel.getState();
        state.setWord("Welcome");
        state.setReachfirst(true);
        state.setReachlast(false);
        state.setPagenumber("0");
        state.setTotalpage("");
        state.setUsername("");
        studySessionViewModel.setState(state);
        studySessionViewModel.firePropertyChanged();

        viewManagerModel.setState(loggedInViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}