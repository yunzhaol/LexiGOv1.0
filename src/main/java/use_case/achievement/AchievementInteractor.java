package use_case.achievement;

import java.util.ArrayList;
import java.util.List;

import entity.Achievement;
import entity.LearnRecord;
import use_case.gateway.UserRecordDataAccessInterface;

/**
 * Interactor that evaluates which achievements a user has unlocked.
 */
public class AchievementInteractor implements AchievementInputBoundary {

    public static final int INT = 3;
    public static final int INT1 = 5;
    public static final int INT2 = 10;

    private final AchievementOutputBoundary presenter;
    private final UserRecordDataAccessInterface userData;

    public AchievementInteractor(AchievementOutputBoundary presenter,
                                 UserRecordDataAccessInterface userData) {
        this.presenter = presenter;
        this.userData = userData;
    }

    @Override
    public void evaluate(AchievementInputData inputData) {
        final String username = inputData.getUsername();
        final List<LearnRecord> wordsLearned = userData.get(username);
        final List<Achievement> newlyUnlocked = new ArrayList<>();

        calculateAchievements(newlyUnlocked, wordsLearned);

        final AchievementOutputData response = new AchievementOutputData(newlyUnlocked);
        presenter.present(response);
    }

    /**
     * Populates {@code newlyUnlocked} with achievements that the user has just met.
     * based on two metrics derived from {@code wordsLearned}:
     * <ul>
     *   <li><strong>totalLearnedTimes</strong> – how many study-session records exist</li>
     *   <li><strong>wordsLearnedCount</strong> – the cumulative number of words learned
     *       across all sessions</li>
     * </ul>
     *
     * <p>The method adds a predefined {@link Achievement} to the list whenever a
     * threshold (1 / 2 / 3 sessions, 1 / 3 / 5 / 10 / 20 words) is reached.</p>
     *
     * @param newlyUnlocked the list into which freshly unlocked achievements are added;
     *                      must not be {@code null}
     * @param wordsLearned  all learning records for the current user; must not be {@code null}
     */
    private void calculateAchievements(List<Achievement> newlyUnlocked,
                                       List<LearnRecord> wordsLearned) {

        final int totalLearnedTimes = wordsLearned.size();
        int wordsLearnedCount = 0;
        for (LearnRecord record : wordsLearned) {
            wordsLearnedCount += record.getLearnedWordIds().size();
        }

        // session-count achievements
        checkTimeLearned(newlyUnlocked, totalLearnedTimes);

        // word-count achievements
        checkWordLearned(newlyUnlocked, wordsLearnedCount);
    }

    private static void checkWordLearned(List<Achievement> newlyUnlocked, int wordsLearnedCount) {
        if (wordsLearnedCount >= 1) {
            newlyUnlocked.add(new Achievement("W1", "First Word",
                    "First Word I Learned!", "\uD83D\uDC4B"));
        }
        if (wordsLearnedCount >= INT) {
            newlyUnlocked.add(new Achievement("W5", "5 Words Learned",
                    "5 Words Learned!", "\uD83C\uDF89"));
        }
        if (wordsLearnedCount >= INT1) {
            newlyUnlocked.add(new Achievement("W10", "10 Words Learned",
                    "10 Words Learned!", "\uD83D\uDD25"));
        }
        if (wordsLearnedCount >= INT2) {
            newlyUnlocked.add(new Achievement("W20", "20 Words Learned",
                    "20 Words Learned!", "\uD83D\uDC53"));
        }
    }

    private static void checkTimeLearned(List<Achievement> newlyUnlocked, int totalLearnedTimes) {
        if (totalLearnedTimes >= 1) {
            newlyUnlocked.add(new Achievement("A1", "1 Time Learned",
                    "You have learned 1 time!", "\uD83D\uDCDA"));
        }
        if (totalLearnedTimes >= 2) {
            newlyUnlocked.add(new Achievement("A2", "2 Times Learned",
                    "You have learned 2 times!", "\uD83E\uDDE0"));
        }
        if (totalLearnedTimes >= INT) {
            newlyUnlocked.add(new Achievement("A3", "3 Times Learned",
                    "You have learned 3 times!", "\uD83C\uDFC6"));
        }
    }
}
