package interface_adapter;

import interface_adapter.finish.FinishController;
import interface_adapter.finish.FinishPresenter;
import interface_adapter.session.LoggedInViewModel;
import interface_adapter.studysession.StudySessionViewModel;
import interface_adapter.studysession.StudySessionState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.finish_checkin.FinishCheckInInputBoundary;
import use_case.finish_checkin.FinishCheckInInputData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Covers:
 * 1) FinishController.execute(...) calls interactor with correct DTO.
 * 2) FinishPresenter.prepareSuccessView() updates StudySessionState and ViewManagerModel,
 *    and fires property-change events.
 */
class FinishIATest {

    private FinishCheckInInputBoundary mockInteractor;
    private LoggedInViewModel loggedInViewModel;
    private ViewManagerModel viewManagerModel;
    private StudySessionViewModel studySessionViewModel;

    private TestListener studyListener;
    private TestListener viewListener;

    @BeforeEach
    void setUp() {
        // 1) mock the interactor boundary
        mockInteractor = mock(FinishCheckInInputBoundary.class);

        // 2) mock logged-in view (only need getViewName())
        loggedInViewModel = mock(LoggedInViewModel.class);
        when(loggedInViewModel.getViewName()).thenReturn("homeView");

        // 3) real ViewModel instances, with listeners attached
        viewManagerModel = new ViewManagerModel();
        studySessionViewModel = new StudySessionViewModel();

        studyListener = new TestListener();
        studySessionViewModel.addPropertyChangeListener(studyListener);

        viewListener = new TestListener();
        viewManagerModel.addPropertyChangeListener(viewListener);
    }

    // ----------------------------------------------------------------
    // Controller: finish → interactor
    // ----------------------------------------------------------------
    @Test
    void controllerExecutesInteractorWithCorrectUsername() {
        FinishController controller = new FinishController(mockInteractor);
        String username = "bob";

        controller.execute(username);

        verify(mockInteractor, times(1))
                .execute(argThat((FinishCheckInInputData in) ->
                        username.equals(in.getUsername())
                ));
    }
    // :contentReference[oaicite:5]{index=5}

    // ----------------------------------------------------------------
    // Presenter: prepareSuccessView → state + viewManager
    // ----------------------------------------------------------------
    @Test
    void presenterPrepareSuccessViewUpdatesModelsAndFiresEvents() {
        FinishPresenter presenter = new FinishPresenter(
                loggedInViewModel,
                viewManagerModel,
                studySessionViewModel
        );

        presenter.prepareSuccessView();

        // 1) StudySessionState fields
        StudySessionState state = studySessionViewModel.getState();
        assertEquals("Welcome",   state.getWord());
        assertTrue(   state.isReachfirst());
        assertFalse(  state.isReachlast());
        assertEquals("0",         state.getPagenumber());
        assertEquals("",          state.getTotalpage());
        assertEquals("",          state.getUsername());

        // listener must have been notified
        assertTrue(studyListener.fired);

        // 2) ViewManagerModel state set to loggedInViewModel.getViewName()
        assertEquals("homeView", viewManagerModel.getState());
        assertTrue(viewListener.fired);
    }
    // :contentReference[oaicite:6]{index=6}:contentReference[oaicite:7]{index=7}

    // ----------------------------------------------------------------
    // Helper: detect firePropertyChanged events
    // ----------------------------------------------------------------
    private static class TestListener implements PropertyChangeListener {
        boolean fired = false;
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            fired = true;
        }
    }
}

