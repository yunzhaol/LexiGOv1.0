package interface_adapter.profile;

import use_case.profile.ProfileOutputBoundary;
import use_case.profile.ProfileOutputData;

public class ProfilePresenter implements ProfileOutputBoundary {
    private final ProfileViewModel profileViewModel;

    public ProfilePresenter(ProfileViewModel profileSetViewModel) {
        this.profileViewModel = profileSetViewModel;
    }

    @Override
    public void prepareSuccessView(ProfileOutputData outputData) {
        final ProfileState profileState = profileViewModel.getState();
        profileState.setLanguageError("");
        profileState.setUsername(outputData.getUsername());
        profileState.setOldlanguage(outputData.getLanguage());
        profileState.setLanguages(outputData.getLanguages());
        profileViewModel.firePropertyChanged();
    }
}
