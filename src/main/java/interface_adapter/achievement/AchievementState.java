package interface_adapter.achievement;

import java.util.ArrayList;
import java.util.List;

/**
 * The State information representing the logged-in user.
 */
public final class AchievementState {
    private List<String> unlockedAchievements = new ArrayList<>();

    /*
    getters
     */
    public List<String> getUnlockedAchievements() {
        return unlockedAchievements;
    }

    /*
    setters
     */
    public void setUnlockedAchievements(List<String> unlockedAchievements) {
        this.unlockedAchievements = unlockedAchievements;
    }
}
