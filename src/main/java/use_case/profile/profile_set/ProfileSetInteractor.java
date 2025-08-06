package use_case.profile.profile_set;

import entity.PersonalProfile;
import entity.ProfileFactory;

public class ProfileSetInteractor implements ProfileSetInputBoundary {
    private final ProfileSetUserDataAccessInterface profileSetUserDataAccessInterface;
    private final ProfileSetOutputBoundary presenter;
    private final ProfileFactory profileFactory;

    public ProfileSetInteractor(ProfileSetUserDataAccessInterface profileSetUserDataAccessInterface,
                                ProfileSetOutputBoundary presenter, ProfileFactory profileFactory) {
        this.profileSetUserDataAccessInterface = profileSetUserDataAccessInterface;
        this.presenter = presenter;
        this.profileFactory = profileFactory;
    }

    @Override
    public void execute(ProfileSetInputData profileSetInputData) {
        if (profileSetInputData.getOldlanguage() == null) {
            final PersonalProfile personalProfile = profileFactory.createPersonalProfile(profileSetInputData
                    .getUsername(), profileSetInputData.getNewlanguage());
            profileSetUserDataAccessInterface.save(personalProfile);
            presenter.prepareSuccessView(new ProfileSetOutputData(profileSetInputData.getUsername(),
                    profileSetInputData.getNewlanguage()));
        }
        else if (profileSetInputData.getOldlanguage().code().equals(profileSetInputData.getNewlanguage().code())) {
            presenter.prepareFailView("New language must be different from the old language.");
        }
        else {
            final PersonalProfile personalProfile = profileFactory
                    .createPersonalProfile(profileSetInputData.getUsername(), profileSetInputData.getNewlanguage());
            profileSetUserDataAccessInterface.save(personalProfile);
            presenter.prepareSuccessView(new ProfileSetOutputData(profileSetInputData.getUsername(),
                    profileSetInputData.getNewlanguage()));
        }
    }
}
