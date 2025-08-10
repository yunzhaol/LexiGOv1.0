package use_case.achievement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Achievement;
import entity.LearnRecord;
import use_case.gateway.UserRecordDataAccessInterface;

/**
 * Interactor that evaluates which achievements a user has unlocked. This class belongs to the "Use
 * Case" layer in Clean Architecture. Its job is to decide whether a user has unlocked any new
 * achievements based on their learning history.
 */
public class AchievementInteractor implements AchievementInputBoundary {
    // These constants represent thresholds for triggering achievements.
    // (INT = 3 sessions, INT1 = 5 words, INT2 = 10 words)
    public static final int INT = 3;
    public static final int INT1 = 5;
    public static final int INT2 = 10;
    public static final int INT3 = 20;
    // This presenter will send the result (output data) to the next layer (e.g., view model).
    private final AchievementOutputBoundary presenter;
    // This is the data access interface used to retrieve a user's learning records.
    private final UserRecordDataAccessInterface userData;

    /**
     * Constructor: injects dependencies needed by the interactor.
     * @param userData presenter.
     * @param presenter presenter.
     */
    public AchievementInteractor(AchievementOutputBoundary presenter,
                                 UserRecordDataAccessInterface userData) {
        this.presenter = presenter;
        this.userData = userData;
    }

    /**
     * The main use case method. It checks whether the user has unlocked
     * any new achievements based on their username.
     */
    @Override
    public void evaluate(AchievementInputData inputData) {
        final String username = inputData.getUsername();
        // Fetch all learning session records for the user.
        final List<LearnRecord> wordsLearned = userData.get(username);
        // This list will store any newly unlocked achievements.
        final List<Achievement> newlyUnlocked = new ArrayList<>();
        // Analyze the data and determine which achievements to unlock.
        calculateAchievements(newlyUnlocked, wordsLearned);
        // Package the result into an output data object and pass it to the presenter.
        final AchievementOutputData response = new AchievementOutputData(newlyUnlocked);
        presenter.present(response);
    }

    /**
     * This helper method calculates which achievements the user should receive
     * @param newlyUnlocked a list to hold newly earned achievements.
     * @param wordsLearned  the user's full learning history.
     */
    private void calculateAchievements(List<Achievement> newlyUnlocked,
                                       List<LearnRecord> wordsLearned) {
        // Number of learning sessions.
        final int totalLearnedTimes = wordsLearned.size();
        // Total number of words learned (sum of word IDs in all records).
        int wordsLearnedCount = 0;
        for (LearnRecord record : wordsLearned) {
            wordsLearnedCount += record.getLearnedWordIds().size();
        }
        // Check if the user qualifies for session-count achievements.
        checkTimeLearned(newlyUnlocked, totalLearnedTimes);
        // Check if the user qualifies for word-count achievements.
        checkWordLearned(newlyUnlocked, wordsLearnedCount);
    }

    /**
     * Adds word-based achievements to the list if thresholds are met.
     * @param newlyUnlocked unlocked
     * @param wordsLearnedCount count
     */
    private void checkWordLearned(List<Achievement> newlyUnlocked, int wordsLearnedCount) {
        final Map<Integer, Achievement> wordAchievements = new HashMap<>();
        wordAchievements.put(1, new Achievement("W1", "First Word",
                "First Word I Learned!", "\uD83D\uDC4B"));
        wordAchievements.put(INT1, new Achievement("W5", "5 Words Learned",
                "5 Words Learned!", "\uD83C\uDF89"));
        wordAchievements.put(INT2, new Achievement("W10", "10 Words Learned",
                "10 Words Learned!", "\uD83D\uDD25"));
        wordAchievements.put(INT3, new Achievement("W20", "20 Words Learned",
                "20 Words Learned!", "\uD83D\uDC53"));

        // If the user's total words learned reaches the threshold, add the achievement.
        for (Map.Entry<Integer, Achievement> entry : wordAchievements.entrySet()) {
            if (wordsLearnedCount >= entry.getKey()) {
                newlyUnlocked.add(entry.getValue());
            }
        }
    }

    /**
     * Adds session-based achievements to the list if thresholds are met.
     * @param newlyUnlocked unlocked
     * @param totalLearnedTimes count
     */
    private void checkTimeLearned(List<Achievement> newlyUnlocked, int totalLearnedTimes) {
        final Map<Integer, Achievement> timeAchievements = new HashMap<>();
        timeAchievements.put(1, new Achievement("A1", "1 Time Learned",
                "You have learned 1 time!", "\uD83D\uDCDA"));
        timeAchievements.put(2, new Achievement("A2", "2 Times Learned",
                "You have learned 2 times!", "\uD83E\uDDE0"));
        timeAchievements.put(INT, new Achievement("A3", "3 Times Learned",
                "You have learned 3 times!", "\uD83C\uDFC6"));

        // If the user's total sessions reach the threshold, add the achievement.
        for (Map.Entry<Integer, Achievement> entry : timeAchievements.entrySet()) {
            if (totalLearnedTimes >= entry.getKey()) {
                newlyUnlocked.add(entry.getValue());
            }
        }
    }
}
