package use_case.profile;

import entity.Language;

public class ProfileInteractor implements ProfileInputBoundary {
    private final ProfileOutputBoundary presenter;
    private final ProfileUserDataAccessInterface  userDataAccess;

    public ProfileInteractor(ProfileOutputBoundary presenter,  ProfileUserDataAccessInterface userDataAccess) {
        this.presenter = presenter;
        this.userDataAccess = userDataAccess;
    }

    @Override
    public void execute(ProfileInputData profileInputData) {
        presenter.prepareSuccessView(new ProfileOutputData(profileInputData.getUsername(), userDataAccess.getLanguage(profileInputData.getUsername()), Language.values()));
    }
}
