package interface_adapter.studysession;

import use_case.studysession.StudySessionInputBoundary;
import use_case.studysession.StudySessionInputData;

public class StudySessionController {

    private final StudySessionInputBoundary interactor;

    public StudySessionController(StudySessionInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void handleNextRequest(String pagenumber, String totalpage) {
        interactor.handleNextRequest(new StudySessionInputData(pagenumber, totalpage));
    }

    public void handlePrevRequest(String pagenumber, String totalpage) {
        interactor.handlePrevRequest(new StudySessionInputData(pagenumber, totalpage));
    }

    public void switchTologgedView() {

    }
}
