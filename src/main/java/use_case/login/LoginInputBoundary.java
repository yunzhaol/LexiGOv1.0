package use_case.login;

/**
 * Input Boundary for actions which are related to logging in.
 */
public interface LoginInputBoundary {

    /**
     * Executes the login use case.
     * @param loginInputData the input data
     */
    void execute(LoginInputData loginInputData);

    /**
     * Navigates from the login view to the sign-up view.
     *
     * <p>
     * Implementations should handle the UI transition or routing logic
     * when the user chooses to register instead of logging in.
     * </p>
     */
    void switchToSignUpView();

}
