package use_case.profile;

import entity.Language;

public interface ProfileUserDataAccessInterface {
    /**
     * Retrieves the stored language preference for the specified user.
     *
     * @param username the unique identifier of the user; must not be {@code null} or blank
     * @return the {@link Language} associated with the user’s profile;
     *         implementations should return a non-null default if none is explicitly set
     * @throws NullPointerException   if {@code username} is {@code null}
     * @throws IllegalArgumentException if {@code username} is blank
     */
    Language getLanguage(String username);
}
