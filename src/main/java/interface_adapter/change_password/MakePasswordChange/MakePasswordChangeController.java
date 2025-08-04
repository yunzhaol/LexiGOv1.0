package interface_adapter.change_password.MakePasswordChange;

import use_case.change_password.make_password_change.MakePasswordChangeInputBoundary;
import use_case.change_password.make_password_change.MakePasswordChangeInputData;

public class MakePasswordChangeController {

    private final MakePasswordChangeInputBoundary makePasswordChangeInteractor;

    public MakePasswordChangeController(MakePasswordChangeInputBoundary makePasswordChangeInteractor) {
        this.makePasswordChangeInteractor = makePasswordChangeInteractor;
    }

    /**
     * Attempts to change the password for a given user.
     *
     * <p>This is the application-layer entry-point that the presenter / controller
     * should call after collecting input from the UI. It simply wraps the raw
     * parameters into a {@link MakePasswordChangeInputData} DTO and delegates the
     * request to the {@code makePasswordChangeInteractor}.</p>
     *
     * @param username        the unique user-name of the account whose password is
     *                        to be changed; must not be {@code null} or blank
     * @param newPassword     the new password in plain text (validation such as
     *                        length / complexity should have been done upstream)
     * @param securityAnswer  the answer to the user’s security question; used by
     *                        the interactor for additional verification
     *
     * @throws NullPointerException      if any argument is {@code null}
     * @throws IllegalArgumentException  if any argument fails basic validation
     *                                   enforced by the interactor
     */
    public void execute(String username,
                        String newPassword,
                        String securityAnswer) {
        makePasswordChangeInteractor.make_password_change(new MakePasswordChangeInputData(username,
                newPassword,
                securityAnswer));
    }
}
