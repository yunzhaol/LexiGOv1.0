package use_case.achievement;

public class AchievementInputData {
    private final String username;

    public AchievementInputData(String username) {

        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}