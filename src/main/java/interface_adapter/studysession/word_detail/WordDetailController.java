package interface_adapter.studysession.word_detail;

import use_case.studysession.word_detail.WordDetailInputBoundary;
import use_case.studysession.word_detail.WordDetailInputData;

public class WordDetailController {
    private WordDetailInputBoundary interactor;

    public WordDetailController(WordDetailInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the word-detail use case for the specified page.
     *
     * @param pagenumber the (1-based) page number to display; must not be {@code null}
     */
    public void execute(String pagenumber) {
        interactor.execute(new WordDetailInputData(pagenumber));
    }

    /**
     * Switches from the word-detail view to the study-session view.
     */
    public void switchToStudySessionView() {
        interactor.switchToStudySessionView();
    }

}
