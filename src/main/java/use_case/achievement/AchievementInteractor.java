package use_case.achievement;

import java.util.ArrayList;
import java.util.List;

public class AchievementInteractor implements AchievementInputBoundary {

    private final AchievementOutputBoundary presenter;
    private final UserProgressDataAccessInterface userData;

    public AchievementInteractor(AchievementOutputBoundary presenter,
                                 UserProgressDataAccessInterface userData) {
        this.presenter = presenter;
        this.userData = userData;
    }

    @Override
    public void evaluate(AchievementInputData requestModel) {
        String userId = requestModel.getUserId();
        int streak = userData.getStreak(userId);
        int wordsLearned = userData.getWordCount(userId);

        List<String> newlyUnlocked = new ArrayList<>();

        if (streak > 0) {
            newlyUnlocked.add("👋 Nice to Meet You");
        }
        if (streak >= 5) {
            newlyUnlocked.add("🔥 5-Day Streak");
        }
        if (streak >= 10) {
            newlyUnlocked.add("🔥 10-Day Streak");
        }
        if (wordsLearned >= 1) {
            newlyUnlocked.add("👋 First Word I Learned");
        }
        if (wordsLearned >= 10) {
            newlyUnlocked.add("🎉 10 Words Learned");
        }

        AchievementOutputData response = new AchievementOutputData(newlyUnlocked);
        presenter.present(response);
        }
}
