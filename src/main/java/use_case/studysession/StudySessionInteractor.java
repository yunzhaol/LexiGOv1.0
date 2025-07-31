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
    public void handleNextRequest(StudySessionInputData in) {

        int currentPage = Integer.parseInt(in.getPagenumber());
        int totalPages  = Integer.parseInt(in.getTotalpage());

        int nextIndex = currentPage;

        String text = deckgetter.getText(nextIndex);

        boolean reachLast  = (nextIndex == totalPages - 1);
        boolean reachFirst = (nextIndex <= 0);

        StudySessionOutputData out = new StudySessionOutputData(
                String.valueOf(currentPage + 1),
                text,
                reachLast,
                reachFirst,
                String.valueOf(totalPages)
        );
        presenter.prepareSuccessView(out);
    }

    @Override
    public void handlePrevRequest(StudySessionInputData in) {

        int currentPage = Integer.parseInt(in.getPagenumber()); // 1‑based
        int totalPages  = Integer.parseInt(in.getTotalpage());

        int prevIndex = currentPage - 2;

        String text = deckgetter.getText(prevIndex);

        boolean reachFirst = (prevIndex == 0);
        boolean reachLast  = false;

        StudySessionOutputData out = new StudySessionOutputData(
                String.valueOf(currentPage - 1),
                text,
                reachLast,
                reachFirst,
                String.valueOf(totalPages)
        );
        presenter.prepareSuccessView(out);
    }
}
