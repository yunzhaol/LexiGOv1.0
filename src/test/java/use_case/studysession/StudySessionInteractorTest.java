package use_case.studysession;

import data_access.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 只验证 Interactor 的业务分支：<br>
 * 1) 词量不足 → presenter.prepareFailView()<br>
 * 2) 词量充足 → presenter.prepareSuccessView() 并持久化卡组
 */
class StudySessionInteractorTest {
    //create a test data )inputData
    // initialize all data access objects
    // give a fake presenter that does nothing but only receives output data and inside it assert output data should be xxxx
    // and then call the interacter method
    //private StudySessionOutputBoundary presenter;
    private UserDeckgetterDataAccessInterface deckgetter;

    @BeforeEach
    void setUp() {
        deckgetter = mock(InMemoryDeckDataAccessObejct.class);
        when(deckgetter.getText(0)).thenReturn("zeal");
        when(deckgetter.getText(2)).thenReturn("last");

    }

    @Test
    void successTest() {
        StudySessionInputData in = new StudySessionInputData("0","3");

        StudySessionOutputBoundary presenter = new StudySessionOutputBoundary() {
            @Override
            public void prepareSuccessView(StudySessionOutputData out) {
                assertNotNull(out);
                assertTrue(out.isReachfirst());
                assertEquals("1", out.getPagenumber());
                assertEquals("3", out.getTotalpage());
                assertEquals("zeal", out.getWordtext());
            }

            @Override
            public void prepareFailureView(StudySessionOutputData out) {

            }
        };

        StudySessionInteractor interactor = new StudySessionInteractor(presenter, deckgetter);
        interactor.handleNextRequest(in);
    }

    @Test
    void failureTest() {
        StudySessionInputData in = new StudySessionInputData("2","3");
        // StartCheckInInputData inNegative = new StartCheckInInputData("tester", "-1");
        StudySessionOutputBoundary presenter = new StudySessionOutputBoundary() {
            @Override
            public void prepareSuccessView(StudySessionOutputData out) {
                assertNotNull(out);
                assertEquals("1", out.getPagenumber());
                assertEquals("3", out.getTotalpage());
                assertEquals("zeal", out.getWordtext());
            }

            @Override
            public void prepareFailureView(StudySessionOutputData out) {

            }
        };
        StudySessionInteractor interactor = new StudySessionInteractor(presenter, deckgetter);
        interactor.handlePrevRequest(in);

    }

    @Test
    void lastTest() {
        StudySessionInputData in = new StudySessionInputData("2","3");
        // StartCheckInInputData inNegative = new StartCheckInInputData("tester", "-1");
        StudySessionOutputBoundary presenter = new StudySessionOutputBoundary() {
            @Override
            public void prepareSuccessView(StudySessionOutputData out) {
                assertNotNull(out);
                assertEquals("3", out.getPagenumber());
                assertEquals("3", out.getTotalpage());
                assertEquals("last", out.getWordtext());
                assertTrue(out.isReachlast());
            }

            @Override
            public void prepareFailureView(StudySessionOutputData out) {

            }
        };
        StudySessionInteractor interactor = new StudySessionInteractor(presenter, deckgetter);
        interactor.handleNextRequest(in);

    }
}
