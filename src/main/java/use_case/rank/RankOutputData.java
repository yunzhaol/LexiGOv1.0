package use_case.rank;

import java.util.List;

public class RankOutputData {

    private final String currentUser;
    private final List<UserScore> leaderboard;
    private final int myPosition;

    public RankOutputData(String currentUser,
                          List<UserScore> leaderboard,
                          int myPosition) {
        this.currentUser = currentUser;
        this.leaderboard = leaderboard;
        this.myPosition = myPosition;
    }

    /**
     * Returns the username of the current user.
     *
     * @return the current user's username
     */
    public String currentUser() {
        return currentUser;
    }

    /**
     * Returns the ranked leaderboard.
     *
     * @return a list of UserScore entries representing the leaderboard
     */
    public List<UserScore> leaderboard() {
        return leaderboard;
    }

    /**
     * Returns the current user's position in the leaderboard.
     *
     * @return the ranking position of the current user
     */
    public int myPosition() {
        return myPosition;
    }
}
