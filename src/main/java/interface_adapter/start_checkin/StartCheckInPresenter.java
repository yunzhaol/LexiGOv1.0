package interface_adapter.start_checkin;

import interface_adapter.ViewManagerModel;
import interface_adapter.session.LoggedInState;
import interface_adapter.session.LoggedInViewModel;
import interface_adapter.studysession.StudySessionState;
import interface_adapter.studysession.StudySessionViewModel;
import use_case.start_checkin.StartCheckInOutputBoundary;
import use_case.start_checkin.StartCheckInOutputData;

public class StartCheckInPresenter implements StartCheckInOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final StartCheckInViewModel startCheckInViewModel;
    private final StudySessionViewModel studySessionViewModel;
    private final LoggedInViewModel loggedInViewModel;

    public StartCheckInPresenter(ViewManagerModel viewManagerModel,
                                 StartCheckInViewModel startCheckInViewModel,
                                 StudySessionViewModel studySessionViewModel,
                                 LoggedInViewModel loggedInViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.startCheckInViewModel = startCheckInViewModel;
        this.studySessionViewModel = studySessionViewModel;
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void prepareSuccessView(StartCheckInOutputData outputData) {
        final LoggedInState statenow = loggedInViewModel.getState();
        final String username = statenow.getUsername();
        final StartCheckInState state = startCheckInViewModel.getState();
        state.setNumberWords("");
        startCheckInViewModel.setState(state);
        startCheckInViewModel.firePropertyChanged();

        final StudySessionState studystate = studySessionViewModel.getState();
        studystate.setUsername(username);
        studystate.setTotalpage(outputData.getTotalpage());
        studystate.setPagenumber("0");
        studystate.setWord("Welcome");
        studySessionViewModel.setState(studystate);
        studySessionViewModel.firePropertyChanged();

        viewManagerModel.setState(studySessionViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final StartCheckInState startCheckInState = startCheckInViewModel.getState();
        startCheckInState.setInputNumberError(error);
        startCheckInViewModel.firePropertyChanged();
    }

    @Override
    public void switchToDeckView() {

    }
}
