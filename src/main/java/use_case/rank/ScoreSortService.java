package use_case.rank;

import java.util.List;
import java.util.Map;

import entity.LearnRecord;

public interface ScoreSortService {
    /**
     * Sorts the provided mapping of usernames to their learn records into a ranked list.
     * Each entry in the resulting list contains a username and its aggregated score.
     *
     * @param input a map where each key is a username and each value is a list of LearnRecord instances
     * @return a list of map entries (username → score), sorted by score in descending order
     */
    List<Map.Entry<String, Integer>> getSortedRank(Map<String, List<LearnRecord>> input);
}
