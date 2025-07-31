package interface_adapter.signup;


import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInputData;
import use_case.signup.SignupSecurityInputBoundary;
import use_case.signup.SignupSecurityInputData;

/**
 * Controller for the Signup Use Case.
 */
public class SignupController {

    private final SignupInputBoundary userSignupUseCaseInteractor;
    public SignupController(SignupInputBoundary userSignupUseCaseInteractor) {
        this.userSignupUseCaseInteractor = userSignupUseCaseInteractor;
    }

    /**
     * Executes the Signup Use Case.
     * @param username the username to sign up
     * @param password1 the password
     * @param password2 the password repeated
     */
    public void execute(String username, String password1, String password2,
                        String securityQuestion, String securityAnswer) {

        if (securityQuestion.isBlank() || securityAnswer.isBlank()) {
            final SignupInputData signupInputData = new SignupInputData(
                    username, password1, password2);
            userSignupUseCaseInteractor.execute(signupInputData);

        } else {
            final SignupSecurityInputData signupSecurityInputData = new SignupSecurityInputData(
                    username, password1, password2, securityQuestion, securityAnswer);
            userSignupUseCaseInteractor.execute(signupSecurityInputData);

        }

    }

    /**
     * Executes the "switch to LoginView" Use Case.
     */
    public void switchToLoginView() {
        userSignupUseCaseInteractor.switchToLoginView();
    }
}
