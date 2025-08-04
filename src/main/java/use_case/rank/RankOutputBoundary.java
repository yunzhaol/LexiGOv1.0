package use_case.rank;

public interface RankOutputBoundary {
    /**
     * Prepares the view for a successful ranking operation.
     *
     * @param rankOutputData the output data containing the leaderboard entries
     *                       and the current user's position; must not be {@code null}
     * @throws NullPointerException if {@code rankOutputData} is {@code null}
     */
    void prepareSuccessView(RankOutputData rankOutputData);
}
