package interface_adapter.signup;

import use_case.signup.common.SignupInputBoundary;
import use_case.signup.common.SignupInputData;
import use_case.signup.security.SignupSecurityInputBoundary;
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
     * Executes a basic sign-up request that does <em>not</em> include any
     * security-question flow.
     *
     * @param username  the desired username; must not be {@code null}
     * @param password1 the first password entry; must not be {@code null}
     * @param password2 the confirmation password entry; must match {@code password1}
     */
    public void signup(String username,
                       String password1,
                       String password2) {
        basicSignupUseCase.execute(
                new SignupInputData(username, password1, password2)
        );
    }

    /**
     * Executes the security-aware sign-up variant.
     *
     * @param username         the desired username; must not be {@code null}
     * @param password1        the first password entry; must not be {@code null}
     * @param password2        the confirmation password entry; must match {@code password1}
     * @param securityQuestion the security question text; must not be {@code null}
     * @param securityAnswer   the answer to the security question; must not be {@code null}
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
