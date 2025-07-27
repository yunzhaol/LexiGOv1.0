package interface_adapter.profile.profile_set;

import entity.Language;
import use_case.profile.profile_set.ProfileSetInputBoundary;
import use_case.profile.profile_set.ProfileSetInputData;


public class ProfileSetController {
    private final ProfileSetInputBoundary interactor;
    public ProfileSetController(ProfileSetInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String username, Language newlanguage, Language oldlanguage) {
        final ProfileSetInputData profileSetInputData = new ProfileSetInputData(username, newlanguage, oldlanguage);

        interactor.execute(profileSetInputData);
    }
}
