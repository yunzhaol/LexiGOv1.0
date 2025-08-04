package use_case.profile.profile_set;

import entity.PersonalProfile;

public interface ProfileSetUserDataAccessInterface {
    /**
     * Saves the given personal profile.
     *
     * @param personalProfile the profile to persist; must not be {@code null}
     * @throws NullPointerException if {@code personalProfile} is {@code null}
     */
    void save(PersonalProfile personalProfile);
}
