package use_case.change_password.make_password_change;

public interface MakePasswordChangeOutputBoundary {
    /**
     * Delivers a failure response.
     *
     * @param out a data object describing why the password change failed;
     *            never {@code null}
     */
    void presentFailure(MakePasswordChangeOutputData out);

    /**
     * Delivers a success response when the password change has completed
     * without error.
     */
    void presentSuccess();
}
