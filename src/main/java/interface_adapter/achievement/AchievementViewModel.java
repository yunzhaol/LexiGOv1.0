package interface_adapter.achievement;

import interface_adapter.ViewModel;

public class AchievementViewModel extends ViewModel<AchievementState> {
    public AchievementViewModel() {
        super("achievement");
        setState(new AchievementState());
    }
}
