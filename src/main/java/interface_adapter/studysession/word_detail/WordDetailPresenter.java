package interface_adapter.studysession.word_detail;

import interface_adapter.ViewManagerModel;
import interface_adapter.studysession.StudySessionViewModel;
import use_case.studysession.word_detail.WordDetailOutputBoundary;
import use_case.studysession.word_detail.WordDetailOutputData;

public class WordDetailPresenter implements WordDetailOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final WordDetailViewModel wordDetailViewModel;
    private final StudySessionViewModel studySessionViewModel;

    public WordDetailPresenter(ViewManagerModel viewManagerModel,
                               WordDetailViewModel wordDetailViewModel,
                               StudySessionViewModel studySessionViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.wordDetailViewModel = wordDetailViewModel;
        this.studySessionViewModel = studySessionViewModel;
    }

    @Override
    public void prepareSuccessView(WordDetailOutputData out) {
        WordDetailState state = wordDetailViewModel.getState();
        state.setExample(out.getExample());
        state.setTranslation(out.getTranslation());
        wordDetailViewModel.setState(state);
        wordDetailViewModel.firePropertyChanged();

        viewManagerModel.setState(wordDetailViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchTologgedView() {

    }

    @Override
    public void switchToStudySessionView() {
        viewManagerModel.setState(studySessionViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
