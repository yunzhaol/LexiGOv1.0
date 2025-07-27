package use_case.studysession.word_detail;

public interface WordDetailOutputBoundary {
    void prepareSuccessView(WordDetailOutputData out);
    void switchTologgedView();
    void switchToStudySessionView();
}
