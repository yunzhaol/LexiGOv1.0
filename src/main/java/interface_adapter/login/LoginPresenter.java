package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.session.LoggedInState;
import interface_adapter.session.LoggedInViewModel;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.start_checkin.StartCheckInState;
import interface_adapter.start_checkin.StartCheckInViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;
    private final SignupViewModel signupViewModel;
    private final StartCheckInViewModel startCheckInViewModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoggedInViewModel loggedInViewModel,
                          LoginViewModel loginViewModel,
                          SignupViewModel signupViewModel, StartCheckInViewModel startCheckInViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
        this.signupViewModel = signupViewModel;
        this.startCheckInViewModel = startCheckInViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // On success, switch to the logged in view.

        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(response.getUsername());
        this.loggedInViewModel.setState(loggedInState);
        this.loggedInViewModel.firePropertyChanged();

        StartCheckInState startState = startCheckInViewModel.getState();
        startState.setUsername(response.getUsername());
        this.startCheckInViewModel.setState(startState);
        this.startCheckInViewModel.firePropertyChanged();

        this.viewManagerModel.setState(loggedInViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.firePropertyChanged();
    }

    @Override
    public void switchToSignUpView() {

        final LoginState loginState = loginViewModel.getState();
        loginState.setPassword("");
        loginState.setUsername("");
        loginViewModel.setState(loginState);
        loginViewModel.firePropertyChanged();

        final SignupState signupState = signupViewModel.getState();
        signupState.setUsername("");
        signupState.setPassword("");
        signupState.setRepeatPassword("");
        signupState.setSecurityQuestion("");
        signupState.setSecurityAnswer("");
        signupState.setUsernameError(null);
        signupViewModel.setState(signupState);
        signupViewModel.firePropertyChanged();

        viewManagerModel.setState(signupViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
