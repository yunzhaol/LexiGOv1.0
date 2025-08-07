package interface_adapter;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.session.LoggedInState;
import interface_adapter.session.LoggedInViewModel;
import interface_adapter.start_checkin.StartCheckInState;
import interface_adapter.start_checkin.StartCheckInViewModel;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;
import use_case.login.LoginOutputData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Covers:
 *  - LoginController.execute(...) + switchToSignUpView()
 *  - LoginPresenter.prepareSuccessView(...)
 *  - LoginPresenter.prepareFailView(...)
 *  - LoginPresenter.switchToSignUpView()
 */

class LoginIATest {

    private LoginInputBoundary mockInteractor;
    private LoginController controller;

    private ViewManagerModel viewManagerModel;
    private LoggedInViewModel loggedInViewModel;
    private LoginViewModel loginViewModel;
    private SignupViewModel signupViewModel;
    private StartCheckInViewModel startCheckInViewModel;

    private TestListener vmListener;
    private TestListener viewListener;

    @BeforeEach
    void setUp() {
        // mock boundary
        mockInteractor        = mock(LoginInputBoundary.class);
        controller            = new LoginController(mockInteractor);  // :contentReference[oaicite:13]{index=13}

        // view-models
        viewManagerModel      = new ViewManagerModel();
        loggedInViewModel     = new LoggedInViewModel();
        loginViewModel        = new LoginViewModel();               // :contentReference[oaicite:14]{index=14}
        signupViewModel       = new SignupViewModel();
        startCheckInViewModel = new StartCheckInViewModel();

        // listeners
        vmListener   = new TestListener();
        viewListener = new TestListener();
        // attach to all VMs that Presenter fires
        loggedInViewModel.addPropertyChangeListener(vmListener);
        startCheckInViewModel.addPropertyChangeListener(vmListener);
        loginViewModel.addPropertyChangeListener(vmListener);
        signupViewModel.addPropertyChangeListener(vmListener);
        viewManagerModel.addPropertyChangeListener(viewListener);
    }

    // Controller.execute(...) → interactor.execute(...)
    @Test
    void execute_delegatesToUseCase() {
        String user = "bob", pass = "hunter2";

        controller.execute(user, pass); // :contentReference[oaicite:15]{index=15}

        verify(mockInteractor, times(1))
                .execute(argThat((LoginInputData in) ->
                        user.equals(in.getUsername()) &&
                                pass.equals(in.getPassword())
                ));
    }

    // Controller.switchToSignUpView() → interactor.switchToSignUpView()
    @Test
    void switchToSignUpView_delegates() {
        controller.switchToSignUpView(); // :contentReference[oaicite:16]{index=16}
        verify(mockInteractor, times(1)).switchToSignUpView();
    }

    // Presenter.prepareSuccessView(...) happy path
    @Test
    void prepareSuccessView_updatesAllAndFires() {
        // arrange
        String username = "alice";
        LoginOutputData out = new LoginOutputData(username, false);

        LoginPresenter presenter = new LoginPresenter(
                viewManagerModel,
                loggedInViewModel,
                loginViewModel,
                signupViewModel,
                startCheckInViewModel
        ); // :contentReference[oaicite:17]{index=17}

        // act
        presenter.prepareSuccessView(out);
        assertFalse(out.isUseCaseFailed());
        // assert LoggedInViewModel
        LoggedInState liState = loggedInViewModel.getState();
        assertEquals(username, liState.getUsername());
        assertTrue(vmListener.fired);
        vmListener.fired = false;

        // assert StartCheckInViewModel
        StartCheckInState scState = startCheckInViewModel.getState();
        assertEquals(username, scState.getUsername());
        assertFalse(vmListener.fired);
        vmListener.fired = false;

        // assert ViewManagerModel switched
        assertEquals(loggedInViewModel.getViewName(), viewManagerModel.getState());
        assertTrue(viewListener.fired);
    }

    // Presenter.prepareFailView(...) error path
    @Test
    void prepareFailView_setsErrorAndFires() {
        String errorMsg = "BAD_CRED";
        LoginPresenter presenter = new LoginPresenter(
                viewManagerModel,
                loggedInViewModel,
                loginViewModel,
                signupViewModel,
                startCheckInViewModel
        ); // :contentReference[oaicite:18]{index=18}

        presenter.prepareFailView(errorMsg);

        assertEquals(errorMsg, loginViewModel.getState().getLoginError());
        assertTrue(vmListener.fired);
    }

    // Presenter.switchToSignUpView() branch
    @Test
    void switchToSignUpView_clearsLoginAndPreparesSignup() {
        // seed loginModel
        loginViewModel.getState().setUsername("x");
        loginViewModel.getState().setPassword("y");
        loginViewModel.setState(loginViewModel.getState());

        LoginPresenter presenter = new LoginPresenter(
                viewManagerModel,
                loggedInViewModel,
                loginViewModel,
                signupViewModel,
                startCheckInViewModel
        ); // :contentReference[oaicite:19]{index=19}

        presenter.switchToSignUpView();

        // login cleared
        assertEquals("", loginViewModel.getState().getUsername());
        assertEquals("", loginViewModel.getState().getPassword());
        assertTrue(vmListener.fired);
        vmListener.fired = false;

        // signup reset
        SignupState s = signupViewModel.getState();
        assertEquals("", s.getUsername());
        assertEquals("", s.getPassword());
        assertNull(s.getUsernameError());
        assertFalse(vmListener.fired);

        // viewManagerModel updated
        assertEquals(signupViewModel.getViewName(), viewManagerModel.getState());
        assertTrue(viewListener.fired);
    }

    // LoginState initial defaults
    @Test
    void loginState_initialValues() {
        LoginState state = new LoginState();                           // :contentReference[oaicite:20]{index=20}
        assertEquals("", state.getUsername());
        assertNull  (state.getLoginError());
        assertEquals("", state.getPassword());
    }

    // LoginViewModel initial setup
    @Test
    void loginViewModel_initial() {
        LoginViewModel vm = new LoginViewModel();                      // :contentReference[oaicite:21]{index=21}
        assertEquals("log in", vm.getViewName());
        assertNotNull(vm.getState());
    }

    private static class TestListener implements PropertyChangeListener {
        boolean fired = false;
        @Override public void propertyChange(PropertyChangeEvent evt) {
            fired = true;
        }
    }
}
