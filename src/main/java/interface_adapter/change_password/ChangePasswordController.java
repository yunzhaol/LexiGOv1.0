package interface_adapter.change_password;

import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInputData;

/**
 * Controller for the Change Password Use Case.
 */
public class ChangePasswordController {
    private final ChangePasswordInputBoundary userChangePasswordUseCaseInteractor;

    public ChangePasswordController(ChangePasswordInputBoundary userChangePasswordUseCaseInteractor) {
        this.userChangePasswordUseCaseInteractor = userChangePasswordUseCaseInteractor;
    }

    /**
     * Initiates the change-password use-case for the specified user.
     *
     * <p>The controller receives the {@code username} from the UI layer,
     * wraps it into a {@link ChangePasswordInputData} DTO and delegates the
     * request to {@code userChangePasswordUseCaseInteractor}.
     * Any further validation or business logic is handled by the interactor.</p>
     *
     * @param username the unique identifier of the user who requests to
     *                 change their password; must not be {@code null} or empty
     *
     * @throws NullPointerException     if {@code username} is {@code null}
     * @throws IllegalArgumentException if {@code username} is blank or fails
     *                                  interactor-level validation
     */
    public void execute(String username) {
        final ChangePasswordInputData changePasswordInputData =
                new ChangePasswordInputData(username);
        userChangePasswordUseCaseInteractor.execute(changePasswordInputData);
    }
}
