package interface_adapter.studysession;

import interface_adapter.ViewModel;

public class StudySessionViewModel extends ViewModel<StudySessionState> {
    public static final String TITLE_LABEL = "Study Session View";

    public StudySessionViewModel() {
        super("study session");
        setState(new StudySessionState());
    }
}
