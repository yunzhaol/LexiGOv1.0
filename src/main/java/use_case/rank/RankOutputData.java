package use_case.rank;

import java.util.List;

public class RankOutputData {


    private final String currentUser;
    private final List<RankEntryData> leaderboard;
    private final int myPosition;

    public RankOutputData(String currentUser,
                          List<RankEntryData> leaderboard,
                          int myPosition) {
        this.currentUser  = currentUser;
        this.leaderboard  = leaderboard;
        this.myPosition   = myPosition;
    }

    public String currentUser()              { return currentUser;  }
    public List<RankEntryData> leaderboard() { return leaderboard;  }
    public int myPosition()                  { return myPosition;   }
}
