package use_case.achievement;

public interface AchievementInputBoundary {

    /**
     * Evaluates the user’s achievements based on the supplied input data.
     *
     * @param achievementInputData encapsulates all state required to perform the
     *                             evaluation; must not be {@code null}
     */
    void evaluate(AchievementInputData achievementInputData);
}
