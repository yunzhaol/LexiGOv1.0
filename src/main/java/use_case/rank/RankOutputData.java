package use_case.rank;

import java.util.List;

public class RankOutputData {

    private final String currentUser;
    private final List<UserScore> leaderboard;
    private final int myPosition;

    public RankOutputData(String currentUser,
                          List<UserScore> leaderboard,
                          int myPosition) {
        this.currentUser  = currentUser;
        this.leaderboard  = leaderboard;
        this.myPosition   = myPosition;
    }

    public String currentUser()              { return currentUser;  }
    public List<UserScore> leaderboard() { return leaderboard;  }
    public int myPosition()                  { return myPosition;   }
}
