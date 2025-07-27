package use_case.achievement;

import java.util.List;

public class AchievementOutputData {
    private final List<String> unlockedAchievements;

    public AchievementOutputData(List<String> unlockedAchievements) {
        this.unlockedAchievements = unlockedAchievements;
    }

    public List<String> getUnlockedAchievements() {
        return unlockedAchievements;
    }
}