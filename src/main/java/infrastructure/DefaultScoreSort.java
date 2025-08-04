package infrastructure;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import entity.LearnRecord;
import use_case.rank.ScoreSortService;

public class DefaultScoreSort implements ScoreSortService {
    @Override
    public List<Map.Entry<String, Integer>> getSortedRank(Map<String, List<LearnRecord>> input) {
        return input.entrySet()
                .stream()
                .map(entry -> {
                    return Map.entry(
                            entry.getKey(),
                            entry.getValue().stream()
                                    .mapToInt(rec -> rec.getLearnedWordIds().size())
                                    .sum());
                })
                .sorted(
                        Comparator.<Map.Entry<String, Integer>, Integer>comparing(Map.Entry::getValue)
                                .reversed()
                                .thenComparing(Map.Entry::getKey))
                .collect(Collectors.toList());

    }
}
