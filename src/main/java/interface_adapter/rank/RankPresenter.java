package interface_adapter.rank;

import use_case.rank.RankOutputBoundary;
import use_case.rank.RankOutputData;


public class RankPresenter implements RankOutputBoundary {
    private final RankViewModel vm;
    public RankPresenter(RankViewModel vm){ this.vm = vm; }

    public void prepareSuccessView(RankOutputData out) {

        RankState state = vm.getState();
        state.setCurrentUser(out.currentUser());
        state.setLeaderboard(out.leaderboard());
        state.setPosition(out.myPosition());
        vm.setState(state);
        vm.firePropertyChanged();
    }
}
