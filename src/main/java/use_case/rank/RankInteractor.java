package use_case.rank;

import infrastructure.DefaultLeaderboardSelector;
import infrastructure.DefaultScoreSort;

import java.util.List;
import java.util.Map;

public class RankInteractor implements RankInputBoundary {

    private final RankUserDataAccessInterface dao;
    private final RankOutputBoundary presenter;
    private final LeaderboardSelector leaderboardSelector;
    private final ScoreSortService scoreSortService;
    private final int limit;

    public RankInteractor(RankUserDataAccessInterface dao, RankOutputBoundary presenter, LeaderboardSelector leaderboardSelector, ScoreSortService scoreSortService, int limit) {
        this.dao = dao;
        this.presenter = presenter;
        this.leaderboardSelector = leaderboardSelector;
        this.scoreSortService = scoreSortService;
        this.limit = limit;
    }

    @Override
    public void execute(RankInputData in) {
        List<Map.Entry<String, Integer>> users = scoreSortService.getSortedRank(dao.getAllUsers());

        LeaderboardStat leaderboardStat = leaderboardSelector.getSortedRank(users, in.getUsername(), limit);

        presenter.prepareSuccessView(
                new RankOutputData(in.getUsername(), leaderboardStat.getRanks(), leaderboardStat.getPosition()));
    }
}
