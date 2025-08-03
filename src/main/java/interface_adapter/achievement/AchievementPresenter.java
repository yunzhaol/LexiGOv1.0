package interface_adapter.achievement;

import entity.Achievement;
import use_case.achievement.AchievementOutputBoundary;
import use_case.achievement.AchievementOutputData;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Presenter for the Achievement feature.
 * Converts the response model from the interactor into a format suitable for the view (via ViewModel).
 */
public class AchievementPresenter implements AchievementOutputBoundary {

    private final AchievementViewModel achievementViewModel;
    //private final ViewManagerModel achievementViewManagerModel;

    public AchievementPresenter(AchievementViewModel achievementViewModel) {
        this.achievementViewModel = achievementViewModel;
        //this.achievementViewManagerModel = achievementViewManagerModel;
    }

    /**
     * Called by the interactor once achievements are evaluated.
     * Updates the ViewModel so the UI can display the new achievements.
     *
     * @param achievementOutputData contains list of newly unlocked achievements
     */
    @Override
    public void present(AchievementOutputData achievementOutputData) {
        AchievementState currentState = achievementViewModel.getState();

        List<String> messages = achievementOutputData.getUnlockedAchievements()
                .stream()
                .map(a -> a.getIconUnicode() + " " + a.getDescription())
                .collect(Collectors.toList());

        currentState.setUnlockedAchievements(messages);
        achievementViewModel.setState(currentState);
        achievementViewModel.firePropertyChanged();
    }

//        achievementViewManagerModel.setState(achievementViewModel.getViewName());  // "achievement"
//        achievementViewManagerModel.firePropertyChanged();
}