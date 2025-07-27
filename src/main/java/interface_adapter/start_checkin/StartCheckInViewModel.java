package interface_adapter.start_checkin;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the Signup View.
 */
public class StartCheckInViewModel extends ViewModel<StartCheckInState> {

    public static final String TITLE_LABEL = "CheckIn View";

    public static final String WORD_NUMBER_LABEL = "Choose number of words to learn";

    public static final String START_BUTTON_LABEL = "Start for today";

    public StartCheckInViewModel() {
        super("check in");
        setState(new StartCheckInState());
    }

}