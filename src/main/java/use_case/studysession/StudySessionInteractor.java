package use_case.studysession;

public class StudySessionInteractor implements StudySessionInputBoundary {

    private final StudySessionOutputBoundary presenter;
    private final UserDeckgetterDataAccessInterface deckgetter;

    public StudySessionInteractor(StudySessionOutputBoundary presenter,
                                  UserDeckgetterDataAccessInterface deckgetter) {
        this.presenter = presenter;
        this.deckgetter = deckgetter;
    }

    @Override
    public void handleNextRequest(StudySessionInputData inputData) {

        final int currentPage = Integer.parseInt(inputData.getPagenumber());
        final int totalPages = Integer.parseInt(inputData.getTotalpage());

        final int nextIndex = currentPage;

        final String text = deckgetter.getText(nextIndex);

        final boolean reachLast = nextIndex == totalPages - 1;
        final boolean reachFirst = nextIndex <= 0;

        final StudySessionOutputData out = new StudySessionOutputData(
                String.valueOf(currentPage + 1),
                text,
                reachLast,
                reachFirst,
                String.valueOf(totalPages)
        );
        presenter.prepareSuccessView(out);
    }

    @Override
    public void handlePrevRequest(StudySessionInputData inputData) {

        final int currentPage = Integer.parseInt(inputData.getPagenumber());
        final int totalPages = Integer.parseInt(inputData.getTotalpage());

        final int prevIndex = currentPage - 2;

        final String text = deckgetter.getText(prevIndex);

        final boolean reachFirst = prevIndex == 0;
        final boolean reachLast = false;

        final StudySessionOutputData out = new StudySessionOutputData(
                String.valueOf(currentPage - 1),
                text,
                reachLast,
                reachFirst,
                String.valueOf(totalPages)
        );
        presenter.prepareSuccessView(out);
    }
}
