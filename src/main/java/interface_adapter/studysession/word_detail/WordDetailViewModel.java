package interface_adapter.studysession.word_detail;

import interface_adapter.ViewModel;

public class WordDetailViewModel extends ViewModel<WordDetailState> {
    public static final String TITLE_LABEL = "Word detail View";

    public WordDetailViewModel() {
        super("word detail");
        setState(new WordDetailState());
    }
}
