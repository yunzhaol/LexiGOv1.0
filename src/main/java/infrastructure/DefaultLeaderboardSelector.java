package infrastructure;

import use_case.rank.LeaderboardSelector;
import use_case.rank.LeaderboardStat;
import use_case.rank.UserScore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultLeaderboardSelector implements LeaderboardSelector {

    @Override
    public LeaderboardStat getSortedRank(List<Map.Entry<String, Integer>> sortedRank, String username, int limit) {
        List<UserScore> top = new ArrayList<>();
        int rank       = 0;
        int prevScore  = Integer.MIN_VALUE;
        int myPosition = -1;

        for (int i = 0; i < sortedRank.size(); i++) {
            Map.Entry<String, Integer> e = sortedRank.get(i);

            if (e.getValue() != prevScore) {
                rank = i + 1;
                prevScore = e.getValue();
            }

            if (rank <= limit) {
                top.add(new UserScore(rank, e.getKey(), e.getValue()));
            }

            if (e.getKey().equals(username)) {
                myPosition = rank;
            }

            if (rank > limit && myPosition != -1) {
                break;
            }
        }

        if (myPosition == -1) {
            myPosition = sortedRank.size() + 1;
        }

        return new LeaderboardStat(myPosition, top);
    }
}
