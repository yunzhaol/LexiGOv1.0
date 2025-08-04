package use_case.change_password;

public interface ChangePasswordUserTypeDataAccessInterface {
    /**
     * Looks up the stored type for the given user.
     *
     * @param username the username to query; never {@code null}
     * @return a string representing the user’s type (e.g. "common", "security")
     */
    String getType(String username);

    /**
     * Retrieves the security question associated with the given user.
     *
     * @param username the username whose question is requested; never {@code null}
     * @return the security question, or {@code null} if none exists
     */
    String getSecurityQuestion(String username);
}
