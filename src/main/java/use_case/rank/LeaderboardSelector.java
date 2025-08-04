package use_case.rank;

import java.util.List;
import java.util.Map;

public interface LeaderboardSelector {
    /**
     * Produces a {@link LeaderboardStat} containing the top-ranked entries
     * (up to {@code limit}) and the position/statistics for a specific user.
     *
     * @param sortedRank a list of entries mapping usernames to scores,
     *                   already sorted in descending order by score;
     *                   must not be {@code null}
     * @param username   the target user's identifier whose position should
     *                   be included; must not be {@code null} or blank
     * @param limit      the maximum number of top entries to include in the
     *                   returned leaderboard; if {@code limit <= 0}, implementations
     *                   may return an empty list or default to a predefined size
     * @return a {@link LeaderboardStat} object containing:
     *         <ul>
     *           <li>a sublist of the top {@code limit} entries,</li>
     *           <li>the specified user's rank and score (even if outside the top list),</li>
     *           <li>and any additional metadata (e.g., total users).</li>
     *         </ul>
     * @throws NullPointerException      if {@code sortedRank} or {@code username} is {@code null}
     * @throws IllegalArgumentException  if {@code username} is blank
     */
    LeaderboardStat getSortedRank(List<Map.Entry<String, Integer>> sortedRank, String username, int limit);
}
