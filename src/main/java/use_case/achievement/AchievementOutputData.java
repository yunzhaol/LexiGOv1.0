package use_case.achievement;

import java.util.List;

import entity.Achievement;

public class AchievementOutputData {
    private final List<Achievement> unlockedAchievements;

    public AchievementOutputData(List<Achievement> unlockedAchievements) {
        this.unlockedAchievements = unlockedAchievements;
    }

    public List<Achievement> getUnlockedAchievements() {
        return unlockedAchievements;
    }
}
