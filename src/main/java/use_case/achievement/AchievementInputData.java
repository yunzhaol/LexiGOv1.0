package use_case.achievement;

public class AchievementInputData {
    private final String userId;

    public AchievementInputData(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}