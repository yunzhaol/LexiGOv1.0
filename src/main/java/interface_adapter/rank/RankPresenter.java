package interface_adapter.rank;

import use_case.rank.RankOutputBoundary;
import use_case.rank.RankOutputData;

public class RankPresenter implements RankOutputBoundary {
    private final RankViewModel viewModel;

    public RankPresenter(RankViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Updates the {@link RankViewModel} with the latest leaderboard information
     * received from the use-case layer and notifies any bound views.
     *
     * @param out the {@link RankOutputData} containing the current user,
     *            leaderboard entries and the user’s position; never {@code null}
     */
    public void prepareSuccessView(RankOutputData out) {
        final RankState state = viewModel.getState();
        state.setCurrentUser(out.currentUser());
        state.setLeaderboard(out.leaderboard());
        state.setPosition(out.myPosition());
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
