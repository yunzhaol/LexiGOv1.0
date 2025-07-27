package interface_adapter.rank;

import interface_adapter.ViewModel;

public class RankViewModel extends ViewModel<RankState> {

    public RankViewModel() {
        super("rank");
        setState(new RankState());
    }
}
