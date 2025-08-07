package interface_adapter.studysession;

import use_case.studysession.StudySessionOutputBoundary;
import use_case.studysession.StudySessionOutputData;

public class StudySessionPresenter implements StudySessionOutputBoundary {

    private final StudySessionViewModel studySessionViewModel;

    public StudySessionPresenter(
                                 StudySessionViewModel studySessionViewModel
                                 ) {

        this.studySessionViewModel = studySessionViewModel;

    }

    @Override
    public void prepareSuccessView(StudySessionOutputData out) {
        final StudySessionState state = studySessionViewModel.getState();
        state.setPagenumber(out.getPagenumber());
        state.setWord(out.getWordtext());
        state.setReachfirst(out.isReachfirst());
        state.setReachlast(out.isReachlast());

        studySessionViewModel.setState(state);
        studySessionViewModel.firePropertyChanged();

    }

    @Override
    public void prepareFailureView(StudySessionOutputData out) {
    }

}
