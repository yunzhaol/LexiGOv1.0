package interface_adapter;

import interface_adapter.change_password.ChangePwState;
import interface_adapter.change_password.ChangeViewModel;
import interface_adapter.change_password.MakePasswordChange.MakePasswordChangeController;
import interface_adapter.change_password.MakePasswordChange.MakePasswordChangePresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.change_password.make_password_change.MakePasswordChangeInputBoundary;
import use_case.change_password.make_password_change.MakePasswordChangeInputData;
import use_case.change_password.make_password_change.MakePasswordChangeOutputData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Tests for MakePasswordChangeController & MakePasswordChangePresenter.
 */
class MakeItChangeIATest {

    private MakePasswordChangeInputBoundary mockInteractor;
    private ChangeViewModel viewModel;
    private TestListener listener;

    @BeforeEach
    void setUp() {
        // 1) mock the interactor boundary
        mockInteractor = mock(MakePasswordChangeInputBoundary.class);
        // 2) real ViewModel + listener
        viewModel = new ChangeViewModel();
        listener = new TestListener();
        viewModel.addPropertyChangeListener(listener);
    }

    // --------------------------------------------------
    // Controller should forward all three args correctly
    // --------------------------------------------------
    @Test
    void controllerCallsInteractorWithCorrectInput() {
        MakePasswordChangeController controller =
                new MakePasswordChangeController(mockInteractor);

        String user          = "alice";
        String newPassword   = "s3cret";
        String securityAnswer= "blue";

        controller.execute(user, newPassword, securityAnswer);

        // capture the input data passed in
        verify(mockInteractor, times(1))
                .makePasswordChange(argThat((MakePasswordChangeInputData in) ->
                        user.equals(in.getUsername())
                                && newPassword.equals(in.getNewPassword())
                                && securityAnswer.equals(in.getSecurityAnswer())
                ));
    }

    // --------------------------------------------
    // Presenter: failure branch (presentFailure)
    // --------------------------------------------
    @Test
    void presenterFailureResetsPasswordAndSetsError() {
        MakePasswordChangePresenter presenter =
                new MakePasswordChangePresenter(viewModel);

        // prepare fake output carrying an error message
        MakePasswordChangeOutputData fakeOut =
                mock(MakePasswordChangeOutputData.class);
        when(fakeOut.getErrorMessage()).thenReturn("WRONG_ANSWER");

        // call the failure path
        presenter.presentFailure(fakeOut);

        ChangePwState state = viewModel.getState();
        // password should be cleared
        assertEquals("", state.getPassword());
        // error message should be propagated
        assertEquals("WRONG_ANSWER", state.getChangeError());
        // listener must have fired
        assertTrue(listener.fired);
    }

    // --------------------------------------------
    // Presenter: success branch (presentSuccess)
    // --------------------------------------------
    @Test
    void presenterSuccessClearsPasswordAndError() {
        // seed the state with non-empty values
        ChangePwState initial = viewModel.getState();
        initial.setPassword("oldpw");
        initial.setChangeError("some error");
        viewModel.setState(initial);

        MakePasswordChangePresenter presenter =
                new MakePasswordChangePresenter(viewModel);

        // call the success path
        presenter.presentSuccess();

        ChangePwState state = viewModel.getState();
        // both fields should be cleared
        assertEquals("", state.getPassword());
        assertEquals("", state.getChangeError());
        // and listener must have fired again
        assertTrue(listener.fired);
    }

    // --------------------------------------------
    // Helper: listens for PropertyChangeEvent
    // --------------------------------------------
    private static class TestListener implements PropertyChangeListener {
        boolean fired = false;
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            fired = true;
        }
    }
}

