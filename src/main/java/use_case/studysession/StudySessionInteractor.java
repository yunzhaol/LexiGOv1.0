package use_case.studysession;

public class StudySessionInteractor implements StudySessionInputBoundary {

    private final StudySessionOutputBoundary presenter;
    private final UserDeckgetterDataAccessInterface deckDao;

    public StudySessionInteractor(StudySessionOutputBoundary presenter,
                                  UserDeckgetterDataAccessInterface deckDao) {
        this.presenter = presenter;
        this.deckDao = deckDao;
    }

    @Override
    public void handleNextRequest(StudySessionInputData inputData) {

        // pagenum && totalpage from input
        final int currentPage = Integer.parseInt(inputData.getPagenumber());
        final int totalPages = Integer.parseInt(inputData.getTotalpage());

        // curr_page = curr_index + 1 = next_index
        final int nextIndex = currentPage;

        // get word using dao(using index)
        final String text = deckDao.getText(nextIndex);

        // if page is first / last As it's simple and do not need extension, so put it here
        final boolean reachLast = nextIndex == totalPages - 1;
        final boolean reachFirst = nextIndex <= 0;

        // output
        final StudySessionOutputData out = new StudySessionOutputData(
                String.valueOf(currentPage + 1),
                text,
                reachLast,
                reachFirst,
                String.valueOf(totalPages)
        );
        // when first page, prev button is unreachable, at last page , next button is unreachable
        // so only success is needed
        presenter.prepareSuccessView(out);
    }

    @Override
    public void handlePrevRequest(StudySessionInputData inputData) {

        final int currentPage = Integer.parseInt(inputData.getPagenumber());
        final int totalPages = Integer.parseInt(inputData.getTotalpage());

        // prev_index = curr_index - 1 = curr_page - 2
        final int prevIndex = currentPage - 2;

        // dao get word
        final String text = deckDao.getText(prevIndex);

        // First && Last status
        final boolean reachFirst = prevIndex == 0;
        final boolean reachLast = false;

        // new output to update
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
