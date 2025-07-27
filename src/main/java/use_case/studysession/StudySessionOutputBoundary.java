package use_case.studysession;

public interface StudySessionOutputBoundary {
    void prepareSuccessView(StudySessionOutputData out);
    void prepareFailureView(StudySessionOutputData out);
    void switchTologgedView();
}
