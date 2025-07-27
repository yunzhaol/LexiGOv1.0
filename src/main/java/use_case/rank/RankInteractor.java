package use_case.rank;

import java.util.*;
import java.util.stream.Collectors;

public class RankInteractor implements RankInputBoundary {

    private final RankUserDataAccessInterface dao;
    private final RankOutputBoundary presenter;
    private static final int RANK_LIMIT = 10;

    public RankInteractor(RankUserDataAccessInterface dao, RankOutputBoundary presenter) {
        this.dao = dao;
        this.presenter = presenter;
    }

    @Override
    public void execute(RankInputData in) {

        List<Map.Entry<String, Integer>> users = dao.getAllUsers().entrySet()
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

        List<RankEntryData> top = new ArrayList<>();
        int rank       = 0;
        int prevScore  = Integer.MIN_VALUE;
        int myPosition = -1;

        for (int i = 0; i < users.size(); i++) {
            Map.Entry<String, Integer> e = users.get(i);

            if (e.getValue() != prevScore) {
                rank = i + 1;
                prevScore = e.getValue();
            }

            if (rank <= RANK_LIMIT) {
                top.add(new RankEntryData(rank, e.getKey(), e.getValue()));
            }

            if (e.getKey().equals(in.getUsername())) {
                myPosition = rank;
            }

            if (rank > RANK_LIMIT && myPosition != -1) break;
        }

        presenter.prepareSuccessView(
                new RankOutputData(in.getUsername(), top, myPosition));
    }
}
