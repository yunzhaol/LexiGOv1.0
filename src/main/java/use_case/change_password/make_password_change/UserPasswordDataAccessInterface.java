package use_case.change_password.make_password_change;

import entity.User;

public interface UserPasswordDataAccessInterface {
    /**
     * Updates the stored user information for the specified username.
     *
     * @param username the username whose data will be updated
     * @param user     the User object containing updated information
     */
    void update(String username, User user);

    /**
     * Retrieves the security answer for the specified username.
     *
     * @param username the username whose security answer is retrieved
     * @return the security answer associated with the username
     */
    String getAnswer(String username);

    /**
     * Retrieves the security question for the specified username.
     *
     * @param username the username whose security question is retrieved
     * @return the security question associated with the username
     */
    String getQuestion(String username);

    /**
     * Returns the user with the given username.
     *
     * @param username the username to look up
     * @return the User with the given username
     */
    User get(String username);
}
