package interface_adapter.achievement;

import java.util.List;
import java.util.stream.Collectors;

import use_case.achievement.AchievementOutputBoundary;
import use_case.achievement.AchievementOutputData;

/**
 * Presenter for the Achievement feature.
 * Converts the response model from the interactor into a format suitable for the view (via ViewModel).
 */
public class AchievementPresenter implements AchievementOutputBoundary {

    private final AchievementViewModel achievementViewModel;

    public AchievementPresenter(AchievementViewModel achievementViewModel) {
        this.achievementViewModel = achievementViewModel;
    }

    /**
     * Called by the interactor once achievements are evaluated.
     * Updates the ViewModel so the UI can display the new achievements.
     *
     * @param achievementOutputData contains list of newly unlocked achievements
     */
    @Override
    public void present(AchievementOutputData achievementOutputData) {
        final AchievementState currentState = achievementViewModel.getState();

        final List<String> messages = achievementOutputData.getUnlockedAchievements()
                .stream()
                .map(achievement -> achievement.getIconUnicode() + " " + achievement.getDescription())
                .collect(Collectors.toList());

        currentState.setUnlockedAchievements(messages);
        achievementViewModel.setState(currentState);
        achievementViewModel.firePropertyChanged();
    }
}
