package use_case.rank;

import entity.LearnRecord;

import java.util.List;
import java.util.Map;

public interface ScoreSortService {
    List<Map.Entry<String, Integer>> getSortedRank(Map<String, List<LearnRecord>> input);
}
