package use_case.change_password.make_password_change;

public interface MakePasswordChangeInputBoundary {
    /**
     * Processes a password change request.
     *
     * @param inputData the input data containing username, new password, and security answer
     */
    void makePasswordChange(MakePasswordChangeInputData inputData);
}
