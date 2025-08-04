package interface_adapter.profile;

import use_case.profile.ProfileInputBoundary;
import use_case.profile.ProfileInputData;

public class ProfileController {
    private final ProfileInputBoundary interactor;

    public ProfileController(ProfileInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the profile-view use case for the specified user.
     *
     * @param username the user whose profile is to be retrieved; must not be {@code null}
     */
    public void execute(String username) {
        final ProfileInputData inputData = new ProfileInputData(username);

        interactor.execute(inputData);
    }
}
