package interface_adapter;

import interface_adapter.studysession.StudySessionController;
import interface_adapter.studysession.StudySessionPresenter;
import interface_adapter.studysession.StudySessionState;
import interface_adapter.studysession.StudySessionViewModel;
import interface_adapter.studysession.word_detail.WordDetailController;
import interface_adapter.studysession.word_detail.WordDetailPresenter;
import interface_adapter.studysession.word_detail.WordDetailState;
import interface_adapter.studysession.word_detail.WordDetailViewModel;
import interface_adapter.view_history.ViewHistoryController;
import interface_adapter.view_history.ViewHistoryPresenter;
import interface_adapter.view_history.ViewHistoryState;
import interface_adapter.view_history.ViewHistoryViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.studysession.StudySessionInteractor;
import use_case.studysession.StudySessionOutputData;
import use_case.studysession.word_detail.WordDetailInteractor;
import use_case.studysession.word_detail.WordDetailOutputData;
import use_case.viewhistory.ViewHistoryInteractor;
import use_case.viewhistory.ViewHistoryOutputData;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

/**
 * Unit tests for study session and word detail related features.
 */
public class StudySessionTest {

    /**
     * Mocked study session interactor.
     */
    private StudySessionInteractor Interactor;

    /**
     * Initializes the mocked interactor before each test.
     */
    @BeforeEach
    public void setUp() {
        Interactor = mock(StudySessionInteractor.class);
    }

    /**
     * Verifies that the controller correctly passes next request to the interactor.
     */
    @Test
    public void viewHistoryTest() {
        StudySessionController controller = new StudySessionController(Interactor);
        controller.handleNextRequest("0", "3");

        // Verifies that handleNextRequest is called with expected input
        verify(Interactor, times(1)).handleNextRequest(argThat(inputData ->
                inputData != null && "0".equals(inputData.getPagenumber()) && "3".equals(inputData.getTotalpage()))
        );
    }

    /**
     * Verifies that the controller correctly passes previous request to the interactor.
     */
    @Test
    public void viewHistory2Test() {
        StudySessionController controller = new StudySessionController(Interactor);
        controller.handlePrevRequest("2", "3");

        // Verifies that handlePrevRequest is called with expected input
        verify(Interactor, times(1)).handlePrevRequest(argThat(inputData ->
                inputData != null && "2".equals(inputData.getPagenumber()) && "3".equals(inputData.getTotalpage()))
        );
    }

    /**
     * Tests the presenter’s success view handling for study session.
     */
    @Test
    public void viewHistory3Test() {
        StudySessionViewModel model = new StudySessionViewModel();
        StudySessionPresenter presenter = new StudySessionPresenter(model);

        presenter.prepareSuccessView(new StudySessionOutputData(
                "1", "word", false, false, "10"
        ));

        StudySessionState state = model.getState();

        // Asserts that state values match expected values
        assertEquals(state.getUsername(), "");
        assertEquals(state.getPagenumber(), "1");
        assertEquals(state.getTotalpage(), null); // Initially null
        assertFalse(state.isReachlast());
        assertFalse(state.isReachfirst());
        assertEquals(state.getWord(), "word");
    }

    /**
     * Tests the presenter’s failure view handling for study session.
     */
    @Test
    public void viewHistory4Test() {
        StudySessionViewModel model = new StudySessionViewModel();
        StudySessionPresenter presenter = new StudySessionPresenter(model);

        presenter.prepareFailureView(new StudySessionOutputData(
                "1", "word", false, false, "10"
        ));

        StudySessionState state = model.getState();

        // On failure, expect reset of page number and total page
        assertEquals(state.getUsername(), "");
        assertEquals(state.getPagenumber(), "0");
        assertEquals(state.getTotalpage(), null);
    }

    /**
     * Tests the StudySessionState class's setter and getter methods.
     */
    @Test
    public void viewHistory5Test() {
        StudySessionState state = new StudySessionState("1", "1", "1", "1", false, false);

        // Confirm initial and updated values
        assertEquals(state.getUsername(), "1");
        state.setUsername("1");
        assertEquals(state.getUsername(), "1");
        state.setUsername("2");
        assertEquals(state.getUsername(), "2");
        state.setTotalpage("10");
        assertEquals(state.getTotalpage(), "10");
    }

    /**
     * Verifies that the word detail controller correctly sends input to the interactor.
     */
    @Test
    public void viewHistory6Test() {
        WordDetailInteractor interactor = mock(WordDetailInteractor.class);
        WordDetailController controller = new WordDetailController(interactor);

        controller.execute("1");

        // Verifies interaction with the interactor
        verify(interactor, times(1)).execute(argThat(inputData ->
                "1".equals(inputData.getCurrpage())));
    }

    /**
     * Tests controller's ability to switch views.
     */
    @Test
    public void viewHistory7Test() {
        WordDetailInteractor interactor = mock(WordDetailInteractor.class);
        WordDetailController controller = new WordDetailController(interactor);

        controller.switchToStudySessionView();

        // Verifies that the interactor handles the view switch
        verify(interactor, times(1)).switchToStudySessionView();
    }

    /**
     * Tests presenter's ability to populate word detail view model.
     */
    @Test
    public void viewHistory8Test() {
        WordDetailViewModel model = new WordDetailViewModel();
        WordDetailPresenter presenter = new WordDetailPresenter(
                new ViewManagerModel(), model, new StudySessionViewModel());

        presenter.prepareSuccessView(new WordDetailOutputData("你好", "Hello man!"));

        WordDetailState state = model.getState();

        // Verifies values were set correctly
        assertEquals(state.getUsername(), "");
        assertEquals(state.getTranslation(), "你好");
        assertEquals(state.getExample(), "Hello man!");
    }

    /**
     * Tests if switching views updates the view manager state.
     */
    @Test
    public void viewHistory9Test() {
        WordDetailViewModel model = new WordDetailViewModel();
        ViewManagerModel model2 = new ViewManagerModel();
        WordDetailPresenter presenter = new WordDetailPresenter(
                model2, model, new StudySessionViewModel());

        presenter.switchToStudySessionView();

        // Verifies view name remains unchanged since no implementation detail updates it
        String viewName = model2.getViewName();
        assertEquals(viewName, "view manager");
    }

    /**
     * Tests getter/setter logic for WordDetailState.
     */
    @Test
    public void viewHistory10Test() {
        WordDetailState state = new WordDetailState("1", "1", "1");

        assertEquals(state.getUsername(), "1");
        state.setUsername("12");
        assertEquals(state.getUsername(), "12");
    }
}
