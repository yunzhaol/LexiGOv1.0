package interface_adapter.profile.profile_set;

import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;
import use_case.profile.profile_set.ProfileSetOutputBoundary;
import use_case.profile.profile_set.ProfileSetOutputData;

public class ProfileSetPresenter implements ProfileSetOutputBoundary {

    private final ProfileViewModel profileViewModel;

    public ProfileSetPresenter(ProfileViewModel profileSetViewModel) {
        this.profileViewModel = profileSetViewModel;
    }

    @Override
    public void prepareSuccessView(ProfileSetOutputData outputData) {
        final ProfileState profileState = profileViewModel.getState();
        profileState.setLanguageError("");
        profileState.setUsername(outputData.getUsername());
        profileState.setOldlanguage(outputData.getNewlanguage());
        profileViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final ProfileState profileState = profileViewModel.getState();
        profileState.setLanguageError(error);
        profileViewModel.firePropertyChanged();
    }
}
