package use_case.achievement;

import entity.LearnRecord;
import use_case.gateway.UserRecordDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class AchievementInteractor implements AchievementInputBoundary {

    private final AchievementOutputBoundary presenter;
    private final UserRecordDataAccessInterface userData;

    public AchievementInteractor(AchievementOutputBoundary presenter,
                                 UserRecordDataAccessInterface userData) {
        this.presenter = presenter;
        this.userData = userData;
    }

    @Override
    public void evaluate(AchievementInputData inputData) {
        String username = inputData.getUsername();
        List<LearnRecord> wordsLearned = userData.get(username);

        List<String> newlyUnlocked = new ArrayList<>();


        extracted(newlyUnlocked, wordsLearned);


        AchievementOutputData response = new AchievementOutputData(newlyUnlocked);
        presenter.present(response);
    }

    private void extracted(List<String> newlyUnlocked, List<LearnRecord> wordsLearned) {
        int totalLearnedTimes = wordsLearned.size();
        int wordsLearnedNumber = 0;
        for (LearnRecord learnedLearned : wordsLearned) {
            wordsLearnedNumber += learnedLearned.getLearnedWordIds().size();
        }
        if (totalLearnedTimes >= 1) {
            newlyUnlocked.add("You have learned 1 time");
        }
        if (totalLearnedTimes >= 2) {
            newlyUnlocked.add("You have learned 2 times");
        }
        if (totalLearnedTimes >= 3) {
            newlyUnlocked.add("You have learned 3 times");
        }
        if (wordsLearnedNumber >= 1) {
            newlyUnlocked.add("First Word I Learned");
        }
        if (wordsLearnedNumber >= 3) {
            newlyUnlocked.add("3 Words Learned");
        }
        if (wordsLearnedNumber >= 5) {
            newlyUnlocked.add("5 Words Learned");
        }
    }
}
