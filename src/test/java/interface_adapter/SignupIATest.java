package interface_adapter;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.login.LoginState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import use_case.signup.common.SignupInputBoundary;
import use_case.signup.common.SignupInputData;
import use_case.signup.security.SignupSecurityInputBoundary;
import use_case.signup.security.SignupSecurityInputData;
import use_case.signup.SignupOutputData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

class SignupIATest {

    private SignupInputBoundary basicBoundary;
    private SignupSecurityInputBoundary securityBoundary;
    private SignupController controller;

    private SignupViewModel signupVM;
    private LoginViewModel  loginVM;
    private ViewManagerModel viewManager;
    private SignupPresenter presenter;

    private TestListener signupListener;
    private TestListener loginListener;
    private TestListener viewListener;

    @BeforeEach
    void setUp() {
        basicBoundary    = mock(SignupInputBoundary.class);
        securityBoundary = mock(SignupSecurityInputBoundary.class);
        controller       = new SignupController(basicBoundary, securityBoundary);

        signupVM    = new SignupViewModel();
        loginVM     = new LoginViewModel();
        viewManager = new ViewManagerModel();
        presenter   = new SignupPresenter(viewManager, signupVM, loginVM);

        signupListener = new TestListener();
        signupVM.addPropertyChangeListener(signupListener);

        loginListener = new TestListener();
        loginVM.addPropertyChangeListener(loginListener);

        viewListener = new TestListener();
        viewManager.addPropertyChangeListener(viewListener);
    }

    // ------------------------------------------------------------
    // Controller: basic signup and security signup flows
    // ------------------------------------------------------------
    @Test
    void basicSignupControllerCallsBasicUseCase() {
        String u = "user", p1 = "pw1", p2 = "pw2";

        controller.signup(u, p1, p2);

        var cap = forClass(SignupInputData.class);
        verify(basicBoundary).execute(cap.capture());
        SignupInputData in = cap.getValue();
        assertAll("basic signup DTO",
                () -> assertEquals(u,  in.getUsername()),
                () -> assertEquals(p1, in.getPassword()),
                () -> assertEquals(p2, in.getRepeatPassword())
        );
    }

    @Test
    void securitySignupControllerCallsSecurityUseCase() {
        String u = "user", p1 = "a", p2 = "b", q = "Q?", a = "A!";
        controller.signupWithSecurity(u, p1, p2, q, a);

        var cap = forClass(SignupSecurityInputData.class);
        verify(securityBoundary).execute(cap.capture());
        SignupSecurityInputData in = cap.getValue();
        assertAll("security signup DTO",
                () -> assertEquals(u, in.getUsername()),
                () -> assertEquals(p1, in.getPassword()),
                () -> assertEquals(p2, in.getRepeatPassword()),
                () -> assertEquals(q,  in.getSecurityQuestion()),
                () -> assertEquals(a,  in.getSecurityAnswer())
        );
    }

    @Test
    void controllerSwitchToLoginViewDelegates() {
        controller.switchToLoginView();
        verify(basicBoundary).switchToLoginView();
    }

    // ------------------------------------------------------------
    // Presenter: success, failure, and switch flows
    // ------------------------------------------------------------
    @Test
    void presenterPrepareSuccessViewUpdatesLoginAndViewManager() {
        SignupOutputData out = mock(SignupOutputData.class);
        when(out.getUsername()).thenReturn("newUser");

        presenter.prepareSuccessView(out);

        // loginVM updated
        LoginState ls = loginVM.getState();
        assertEquals("newUser", ls.getUsername());
        assertTrue(loginListener.fired);

        // viewManager updated
        assertEquals(loginVM.getViewName(), viewManager.getState());
        assertTrue(viewListener.fired);
    }

    @Test
    void presenterPrepareFailViewUpdatesSignupStateAndFires() {
        presenter.prepareFailView("ERR_MSG");

        SignupState ss = signupVM.getState();
        assertEquals("ERR_MSG", ss.getUsernameError());
        assertTrue(signupListener.fired);
    }

    @Test
    void presenterSwitchToLoginViewUpdatesOnlyViewManager() {
        presenter.switchToLoginView();
        assertEquals(loginVM.getViewName(), viewManager.getState());
        assertTrue(viewListener.fired);
    }

    // ------------------------------------------------------------
    // SignupState defaults, getters/setters, toString
    // ------------------------------------------------------------
    @Test
    void signupStateDefaultsAndToString() {
        SignupState s = new SignupState();
        assertAll("defaults",
                () -> assertEquals("", s.getUsername()),
                () -> assertNull(s.getUsernameError()),
                () -> assertEquals("", s.getPassword()),
                () -> assertNull(s.getPasswordError()),
                () -> assertEquals("", s.getRepeatPassword()),
                () -> assertNull(s.getRepeatPasswordError()),
                () -> assertEquals("", s.getSecurityQuestion()),
                () -> assertEquals("", s.getSecurityAnswer())
        );

        s.setUsername("u");
        s.setPassword("p");
        s.setRepeatPassword("r");
        String repr = s.toString();
        assertAll("toString contains fields",
                () -> assertTrue(repr.contains("username='u'")),
                () -> assertTrue(repr.contains("password='p'")),
                () -> assertTrue(repr.contains("repeatPassword='r'"))
        );
    }

    // ------------------------------------------------------------
    // SignupViewModel initial wiring
    // ------------------------------------------------------------
    @Test
    void signupViewModelInitialStateAndViewName() {
        assertAll("viewModel wiring",
                () -> assertEquals("sign up", signupVM.getViewName()),
                () -> assertNotNull(signupVM.getState())
        );
    }

    // ------------------------------------------------------------
    // Helper listener to detect PropertyChangeEvent
    // ------------------------------------------------------------
    private static class TestListener implements PropertyChangeListener {
        boolean fired = false;
        @Override public void propertyChange(PropertyChangeEvent evt) {
            fired = true;
        }
    }
    @Test
    void signupStateAdditionalSettersAndGettersWork() {
        SignupState s = new SignupState();

        // passwordError
        s.setPasswordError("badPass");
        assertEquals("badPass", s.getPasswordError(), "passwordError getter/setter");

        // repeatPassword
        s.setRepeatPassword("again123");
        assertEquals("again123", s.getRepeatPassword(), "repeatPassword getter/setter");

        // repeatPasswordError
        s.setRepeatPasswordError("noMatch");
        assertEquals("noMatch", s.getRepeatPasswordError(), "repeatPasswordError getter/setter");

        // securityQuestion / securityAnswer
        s.setSecurityQuestion("Your pet?");
        assertEquals("Your pet?", s.getSecurityQuestion(), "securityQuestion getter/setter");

        s.setSecurityAnswer("fluffy");
        assertEquals("fluffy", s.getSecurityAnswer(), "securityAnswer getter/setter");
    }
}
