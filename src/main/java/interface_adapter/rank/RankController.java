package interface_adapter.rank;

import use_case.rank.RankInputBoundary;
import use_case.rank.RankInputData;

public class RankController {
    private RankInputBoundary rankInteractor;

    public RankController(RankInputBoundary rankInteractor) {
        this.rankInteractor = rankInteractor;
    }

    /**
     * Executes the Logout Use Case.
     * @param username the username of the user logging in
     */
    public void execute(String username) {
        final RankInputData rankInputData = new RankInputData(username);
        this.rankInteractor.execute(rankInputData);
    }
}
