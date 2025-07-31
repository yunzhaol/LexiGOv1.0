package interface_adapter.rank;

import use_case.rank.UserScore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankState {

    private List<UserScore> leaderboard = Collections.synchronizedList(new ArrayList<>());
    private String currentUser = "";
    private int position = 0;

    /* ---------- Getter ---------- */
    public List<UserScore> getLeaderboard() { return leaderboard; }

    public String getCurrentUser() { return currentUser; }

    public int getPosition() { return position; }

    public void setLeaderboard(List<UserScore> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
