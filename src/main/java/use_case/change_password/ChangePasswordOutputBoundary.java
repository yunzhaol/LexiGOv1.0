package use_case.change_password;

/**
 * The output boundary for the Change Password Use Case.
 */
public interface ChangePasswordOutputBoundary {
    /**
     * Prepares the change-password page using the supplied data.
     *
     * @param out the output data containing the username, verification status,
     *            and (optionally) the security question; never {@code null}
     */
    void preparePage(ChangePasswordOutputData out);
}
