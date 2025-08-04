package interface_adapter.studysession;

import use_case.studysession.StudySessionInputBoundary;
import use_case.studysession.StudySessionInputData;

public class StudySessionController {

    private final StudySessionInputBoundary interactor;

    public StudySessionController(StudySessionInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Handles a request to advance to the next study-session page.
     *
     * @param pagenumber the current (1-based) page number; never {@code null}
     * @param totalpage  the total number of pages in the session; never {@code null}
     */
    public void handleNextRequest(String pagenumber, String totalpage) {
        interactor.handleNextRequest(new StudySessionInputData(pagenumber, totalpage));
    }

    /**
     * Handles a request to return to the previous study-session page.
     *
     * @param pagenumber the current (1-based) page number; never {@code null}
     * @param totalpage  the total number of pages in the session; never {@code null}
     */
    public void handlePrevRequest(String pagenumber, String totalpage) {
        interactor.handlePrevRequest(new StudySessionInputData(pagenumber, totalpage));
    }
}
