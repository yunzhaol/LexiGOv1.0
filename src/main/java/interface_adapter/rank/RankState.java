package interface_adapter.rank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import use_case.rank.UserScore;

public final class RankState {

    private List<UserScore> leaderboard = Collections.synchronizedList(new ArrayList<>());
    private String currentUser = "";
    private int position;

    /* ---------- Getter ---------- */
    public List<UserScore> getLeaderboard() {
        return leaderboard;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public int getPosition() {
        return position;
    }

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
