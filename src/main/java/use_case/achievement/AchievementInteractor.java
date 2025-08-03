package use_case.achievement;

import entity.Achievement;
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

        List<Achievement> newlyUnlocked = new ArrayList<>();


        extracted(newlyUnlocked, wordsLearned);


        AchievementOutputData response = new AchievementOutputData(newlyUnlocked);
        presenter.present(response);
    }

    private void extracted(List<Achievement> newlyUnlocked, List<LearnRecord> wordsLearned) {
        int totalLearnedTimes = wordsLearned.size();
        int wordsLearnedNumber = 0;
        for (LearnRecord learnedLearned : wordsLearned) {
            wordsLearnedNumber += learnedLearned.getLearnedWordIds().size();
        }
        if (totalLearnedTimes >= 1) {
            newlyUnlocked.add(new Achievement(
                    "A1", "1 Time Learned", "You have learned 1 time!", "1⃣️"
            ));
        }
        if (totalLearnedTimes >= 2) {
            newlyUnlocked.add(new Achievement(
                    "A2", "2 Times Learned", "You have learned 2 times!", "2⃣️"
            ));
        }
        if (totalLearnedTimes >= 3) {
            newlyUnlocked.add(new Achievement(
                    "A3", "3 Times Learned", "You have learned 3 times!", "3⃣️"
            ));
        }

        if (wordsLearnedNumber >= 1) {
            newlyUnlocked.add(new Achievement(
                    "W1", "First Word", "First Word I Learned!", "👋"
            ));
        }
        if (wordsLearnedNumber >= 3) {
            newlyUnlocked.add(new Achievement(
                    "W5", "5 Words Learned", "5 Words Learned!", "🎉"
            ));
        }
        if (wordsLearnedNumber >= 5) {
            newlyUnlocked.add(new Achievement(
                    "W10", "10 Words Learned", "10 Words Learned!", "🔥"
            ));
        }
        if (wordsLearnedNumber >= 10) {
            newlyUnlocked.add(new Achievement(
                    "W20", "20 Words Learned", "20 Words Learned!", "👓"
            ));
        }
    }
}

