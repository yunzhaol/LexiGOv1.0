package use_case.achievement;

public interface AchievementDataAccessInterface {
    int getStreak(String userId);
    int getWordCount(String userId);
    //TODO: update later
}
