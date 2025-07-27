package use_case.studysession.word_detail;

public interface WordDetailInputBoundary {
    void execute(WordDetailInputData in);
    void switchTologgedView();
    void switchToStudySessionView();
}
