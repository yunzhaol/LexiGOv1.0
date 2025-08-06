package interface_adapter;

import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.session.LoggedInState;
import interface_adapter.session.LoggedInViewModel;
import org.junit.jupiter.api.Test;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInputData;
import use_case.logout.LogoutOutputData;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class LogoutIATest {

    @Test
    void controllerExecute_forwardsUsernameToInteractor() {
        // Arrange
        LogoutInputBoundary mockInteractor = mock(LogoutInputBoundary.class);
        LogoutController controller = new LogoutController(mockInteractor);
        String username = "testUser";

        // Act
        controller.execute(username);

        // Assert
        verify(mockInteractor, times(1))
                .execute(argThat((LogoutInputData in) ->
                        in != null && username.equals(in.getUsername())
                ));
    }

    @Test
    void presenterPrepareSuccessView_clearsStatesAndFires() {
        // Arrange: mocks for all three view‐models
        LoggedInViewModel  loggedInVM  = mock(LoggedInViewModel.class);
        LoggedInState      liState    = new LoggedInState();
        when(loggedInVM.getState()).thenReturn(liState);

        LoginViewModel     loginVM     = mock(LoginViewModel.class);
        LoginState         loState     = new LoginState();
        when(loginVM.getState()).thenReturn(loState);

        ViewManagerModel   vmm         = mock(ViewManagerModel.class);

        LogoutPresenter presenter = new LogoutPresenter(vmm, loggedInVM, loginVM);
        LogoutOutputData out = mock(LogoutOutputData.class);

        // Act
        presenter.prepareSuccessView(out);

        // Assert: logged-in state cleared & VM updated
        assertEquals("", liState.getUsername());
        verify(loggedInVM).setState(liState);
        verify(loggedInVM).firePropertyChanged("state");

        // Assert: login state cleared & VM updated
        assertEquals("", loState.getUsername());
        assertEquals("", loState.getPassword());
        verify(loginVM).setState(loState);
        verify(loginVM).firePropertyChanged();

        // Assert: view manager switched to login view name
        verify(vmm).setState(loginVM.getViewName());
        verify(vmm).firePropertyChanged();
    }

    @Test
    void presenterPrepareFailView_doesNothing() {
        // Arrange
        LoggedInViewModel  loggedInVM  = mock(LoggedInViewModel.class);
        LoginViewModel     loginVM     = mock(LoginViewModel.class);
        ViewManagerModel   vmm         = mock(ViewManagerModel.class);

        LogoutPresenter presenter = new LogoutPresenter(vmm, loggedInVM, loginVM);

        // Act & Assert: no exception, no interactions beyond the interface
        assertDoesNotThrow(() -> presenter.prepareFailView("anyError"));
        // Optionally verify no further calls were made:
        verifyNoMoreInteractions(loggedInVM, loginVM, vmm);
    }
}
