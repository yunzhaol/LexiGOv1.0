package use_case.achievement;

public interface UserProgressDataAccessInterface {
    int getStreak(String userId);
    int getWordCount(String userId);

    /*
    这里需要checkin和vocab来implement此interface，这样才能update achievement
     */
}
