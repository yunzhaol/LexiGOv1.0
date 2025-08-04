package interface_adapter.profile.profile_set;

import entity.Language;
import use_case.profile.profile_set.ProfileSetInputBoundary;
import use_case.profile.profile_set.ProfileSetInputData;

public class ProfileSetController {
    private final ProfileSetInputBoundary interactor;

    public ProfileSetController(ProfileSetInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the profile-language‐update use case for the specified user.
     *
     * @param username     the user whose language preference is being updated;
     *                     must not be {@code null}
     * @param newlanguage  the new preferred {@link Language}; may be {@code null}
     * @param oldlanguage  the user’s current {@link Language}; may be {@code null}
     */
    public void execute(String username, Language newlanguage, Language oldlanguage) {
        final ProfileSetInputData profileSetInputData = new ProfileSetInputData(username, newlanguage, oldlanguage);

        interactor.execute(profileSetInputData);
    }
}
