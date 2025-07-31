package use_case.rank;

import java.util.List;
import java.util.Map;

public interface LeaderboardSelector {
    LeaderboardStat getSortedRank(List<Map.Entry<String, Integer>> sortedRank, String username, int limit);
}
