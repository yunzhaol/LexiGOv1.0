package interface_adapter.studysession.word_detail;

import use_case.studysession.word_detail.WordDetailInputBoundary;
import use_case.studysession.word_detail.WordDetailInputData;

public class WordDetailController {
    private WordDetailInputBoundary interactor;

    public WordDetailController(WordDetailInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String pagenumber) {
        interactor.execute(new WordDetailInputData(pagenumber));
    }

    public void switchTologgedView() {

    }

    public void switchToStudySessionView() {
        interactor.switchToStudySessionView();
    }
}
