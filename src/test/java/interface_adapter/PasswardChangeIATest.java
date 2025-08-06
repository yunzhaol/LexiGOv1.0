package interface_adapter;

import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.change_password.ChangePasswordPresenter;
import interface_adapter.change_password.ChangePwState;
import interface_adapter.change_password.ChangeViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInputData;
import use_case.change_password.ChangePasswordOutputData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Covers the Interface Adapter layer for the Change Password use case:
 * Controller, Presenter, ViewModel, and State.
 */
@DisplayName("Change Password Interface Adapter Tests")
class PasswardChangeIATest {

    private ChangePasswordInputBoundary mockInteractor;
    private ChangeViewModel viewModel;
    private TestListener listener;

    @BeforeEach
    void setUp() {
        // 1) mock the Interactor boundary
        mockInteractor = mock(ChangePasswordInputBoundary.class);
        // 2) real ViewModel + listener to catch change events
        viewModel = new ChangeViewModel();
        listener = new TestListener();
        viewModel.addPropertyChangeListener(listener);
    }

    // ----------------------------
    // Controller tests
    // ----------------------------
    @Test
    @DisplayName("Controller should call use-case with correct username")
    void controllerCallsUseCaseWithCorrectUsername() {
        ChangePasswordController controller = new ChangePasswordController(mockInteractor);
        String username = "johnDoe";

        // act
        controller.execute(username);

        // assert
        verify(mockInteractor, times(1))
                .execute(argThat((ChangePasswordInputData input) ->
                        input != null && username.equals(input.getUsername())
                ));
    }

    // ----------------------------
    // ViewModel/State initial values
    // ----------------------------
    @Test
    @DisplayName("Initial ViewModel state should have all empty/default fields")
    void viewModelInitialStateIsEmpty() {
        ChangePwState state = viewModel.getState();
        assertEquals("",  state.getUsername(),        "Initial username should be empty");
        assertEquals("",  state.getPassword(),        "Initial password should be empty");
        assertEquals("",  state.getChangeError(),     "Initial changeError should be empty");
        assertFalse(state.isVerification(),           "Initial verification flag should be false");
        assertEquals("",  state.getSecurityQuestion(), "Initial securityQuestion should be empty");
    }

    // ----------------------------
    // Presenter tests — success branch
    // ----------------------------
    @Test
    @DisplayName("Presenter should populate state and fire change event on success")
    void presenterPopulatesStateAndFiresEvent() {
        ChangePasswordPresenter presenter = new ChangePasswordPresenter(viewModel);

        // mock output data
        ChangePasswordOutputData output = mock(ChangePasswordOutputData.class);
        when(output.getUsername()).thenReturn("alice");
        when(output.isNeedVerified()).thenReturn(true);
        when(output.getSecurityQuestion()).thenReturn("What is your pet?");

        // act
        presenter.preparePage(output);

        // verify state was updated
        ChangePwState state = viewModel.getState();
        assertEquals("alice",      state.getUsername());
        assertTrue(state.isVerification());
        assertEquals("What is your pet?", state.getSecurityQuestion());
        assertEquals("",           state.getPassword(),    "Password should be reset to empty");
        assertEquals("",           state.getChangeError(), "changeError should remain default");

        // verify that firePropertyChanged() was invoked
        assertTrue(listener.fired, "firePropertyChanged() should be called");
    }

    // ----------------------------
    // Presenter tests — null/default branch
    // ----------------------------
    @Test
    @DisplayName("Presenter should handle null/default output without NPE")
    void presenterHandlesNullOrDefaultGracefully() {
        ChangePasswordPresenter presenter = new ChangePasswordPresenter(viewModel);

        // by not stubbing, output returns null/false/default values
        ChangePasswordOutputData output = mock(ChangePasswordOutputData.class);
        when(output.getUsername()).thenReturn(null);
        when(output.isNeedVerified()).thenReturn(false);
        when(output.getSecurityQuestion()).thenReturn(null);

        // act
        presenter.preparePage(output);

        // state should reflect the null/false values from output
        ChangePwState state = viewModel.getState();
        assertNull(state.getUsername());
        assertFalse(state.isVerification());
        assertNull(state.getSecurityQuestion());
        assertEquals("", state.getPassword());
        assertEquals("", state.getChangeError());

        // even when input is null, firePropertyChanged() should still be invoked
        assertTrue(listener.fired, "firePropertyChanged() should be called even on null/default");
    }

    // ----------------------------
    // Helper listener to detect PropertyChangeEvent
    // ----------------------------
    private static class TestListener implements PropertyChangeListener {
        boolean fired = false;
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            fired = true;
        }
    }
}
