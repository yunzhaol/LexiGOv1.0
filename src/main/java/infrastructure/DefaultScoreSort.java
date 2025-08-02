package infrastructure;

import entity.LearnRecord;
import use_case.rank.ScoreSortService;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultScoreSort implements ScoreSortService {
    @Override
    public List<Map.Entry<String, Integer>> getSortedRank(Map<String, List<LearnRecord>> input) {
        return input.entrySet()
                .stream()
                .map(e -> new AbstractMap.SimpleEntry<>(
                        e.getKey(),
                        e.getValue().stream()
                                .mapToInt(rec -> rec.getLearnedWordIds().size())
                                .sum()))
                .sorted((a, b) -> {
                    int cmp = Integer.compare(b.getValue(), a.getValue());
                    return (cmp != 0) ? cmp : a.getKey().compareTo(b.getKey());
                })
                .collect(Collectors.toList());
    }
}
