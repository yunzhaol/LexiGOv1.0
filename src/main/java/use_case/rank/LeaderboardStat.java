package use_case.rank;

import java.util.List;

public final class LeaderboardStat {
    private final int position;
    private final List<UserScore> ranks;

    public LeaderboardStat(int position, List<UserScore> ranks) {
        this.position = position;
        this.ranks = ranks;
    }

    public int getPosition() {
        return position;
    }

    public List<UserScore> getRanks() {
        return ranks;
    }
}
