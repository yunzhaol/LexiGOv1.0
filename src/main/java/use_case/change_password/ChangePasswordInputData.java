package use_case.change_password;

/**
 * The input data for the Change Password Use Case.
 */
public final class ChangePasswordInputData {

    private final String username;

    public ChangePasswordInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
