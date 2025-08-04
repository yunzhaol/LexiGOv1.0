package use_case.studysession.word_detail;

public class WordDetailInteractor implements WordDetailInputBoundary {

    private final WordDetailOutputBoundary presenter;
    private final WordDetailDataAccessInterface wordDetailDataAccess;

    public WordDetailInteractor(WordDetailOutputBoundary presenter,
                                WordDetailDataAccessInterface wordDetailDataAccess) {
        this.presenter = presenter;
        this.wordDetailDataAccess = wordDetailDataAccess;
    }

    @Override
    public void execute(WordDetailInputData inputData) {
        final String translation = wordDetailDataAccess.getTranslation(
                Integer.parseInt(inputData.getCurrpage()) - 1);
        final String example = wordDetailDataAccess.getExample(
                Integer.parseInt(inputData.getCurrpage()) - 1);

        final WordDetailOutputData wordDetailOutputData = new WordDetailOutputData(translation, example);
        presenter.prepareSuccessView(wordDetailOutputData);
    }

    @Override
    public void switchTologgedView() {

    }

    @Override
    public void switchToStudySessionView() {
        presenter.switchToStudySessionView();
    }
}
