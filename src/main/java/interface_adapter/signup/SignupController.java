package interface_adapter.signup;

import use_case.signup.common.SignupInputBoundary;
import use_case.signup.security.SignupSecurityInputBoundary;
import use_case.signup.common.SignupInputData;
import use_case.signup.security.SignupSecurityInputData;

/**
 * Controller for the Signup Use Case.
 */
public class SignupController {

    private final SignupInputBoundary basicSignupUseCase;
    private final SignupSecurityInputBoundary securitySignupUseCase;

    public SignupController(SignupInputBoundary basicSignupUseCase,
                            SignupSecurityInputBoundary securitySignupUseCase) {
        this.basicSignupUseCase = basicSignupUseCase;
        this.securitySignupUseCase = securitySignupUseCase;
    }

    /**
     * Executes basic signup without security questions.
     */
    public void signup(String username,
                       String password1,
                       String password2) {
        basicSignupUseCase.execute(
                new SignupInputData(username, password1, password2)
        );
    }

    /**
     * Executes signup with security questions.
     */
    public void signupWithSecurity(String username,
                                   String password1,
                                   String password2,
                                   String securityQuestion,
                                   String securityAnswer) {
        securitySignupUseCase.execute(
                new SignupSecurityInputData(
                        username,
                        password1,
                        password2,
                        securityQuestion,
                        securityAnswer
                )
        );
    }

    /**
     * Executes the "switch to LoginView" Use Case.
     */
    public void switchToLoginView() {
        basicSignupUseCase.switchToLoginView();
    }
}
