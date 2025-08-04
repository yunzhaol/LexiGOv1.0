package use_case.achievement;

public interface AchievementOutputBoundary {

    /**
     * Delivers the outcome of an achievement-evaluation operation.
     *
     * @param responseModel a DTO containing the list of achievements
     *                      that have just been unlocked; must not be {@code null}
     */
    void present(AchievementOutputData responseModel);
}
