package use_case.studysession;

public interface StudySessionInputBoundary {

    void handleNextRequest(StudySessionInputData in);
    void handlePrevRequest(StudySessionInputData in);
    //void switchTologgedView();
}
