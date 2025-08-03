package use_case.achievement;

import entity.Achievement;

import java.util.List;

public class AchievementOutputData {
    private final List<Achievement> unlockedAchievements;

    public AchievementOutputData(List<Achievement> unlockedAchievements) {
        this.unlockedAchievements = unlockedAchievements;
    }

    public List<Achievement> getUnlockedAchievements() {
        return unlockedAchievements;
    }
}