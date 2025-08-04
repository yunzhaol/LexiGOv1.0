package entity;

public interface ProfileFactory {
    /**
     * Creates a {@link entity.PersonalProfile} for the specified user.
     *
     * @param username the username to associate with the profile; never {@code null}
     * @param language the preferred {@link entity.Language}; never {@code null}
     * @return a fully initialised {@link entity.PersonalProfile}
     */
    PersonalProfile createPersonalProfile(String username, Language language);
}
