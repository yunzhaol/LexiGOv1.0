package view;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import interface_adapter.finish.FinishController;
import interface_adapter.studysession.StudySessionController;
import interface_adapter.studysession.StudySessionState;
import interface_adapter.studysession.StudySessionViewModel;
import interface_adapter.studysession.word_detail.WordDetailController;

/**
 * Shows the current study card (word) and provides navigation controls
 * for the deck created by the initial check-in step.
 *
 * <p>
 * This view listens to {@link StudySessionViewModel} state changes and updates
 * the text labels and button states accordingly. Controllers are injected to
 * handle navigation, flip, and finish actions.
 * </p>
 */
public class StudySessionView extends JPanel implements PropertyChangeListener {

    // ---- Constants (eliminate magic numbers & non-ASCII) ----
    private static final float TITLE_FONT_SIZE = 24f;
    private static final int GAP_TOP = 70;
    private static final int GAP_BETWEEN = 10;
    private static final int GAP_BELOW = 39;

    private static final String TITLE_TEXT = "Daily Check-In";
    private static final String BTN_PREV = "Prev";
    private static final String BTN_NEXT = "Next";
    private static final String BTN_FLIP = "Flip";
    private static final String BTN_FINISH = "Finish";

    // ---- View identity ----
    private final String viewName = "study session";

    // ---- Collaborators ----
    private final StudySessionViewModel viewModel;

    // ---- Components ----
    private final JLabel wordLabel = new JLabel("Word", SwingConstants.CENTER);
    private final JLabel progressLabel = new JLabel("0 / 0", SwingConstants.CENTER);

    private final JButton prev = new JButton(BTN_PREV);
    private final JButton next = new JButton(BTN_NEXT);
    private final JButton flip = new JButton(BTN_FLIP);
    private final JButton finish = new JButton(BTN_FINISH);

    private WordDetailController wordDetailController;
    private StudySessionController navController;
    private FinishController finishController;

    /**
     * Creates the study-session view and binds it to the given view model.
     *
     * @param viewModel the view model to observe
     */
    public StudySessionView(final StudySessionViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addPropertyChangeListener(this);

        wordLabel.setFont(wordLabel.getFont().deriveFont(TITLE_FONT_SIZE));
        wordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel nav = new JPanel();
        nav.add(prev);
        nav.add(next);
        nav.add(flip);
        nav.add(finish);
        nav.setAlignmentX(Component.CENTER_ALIGNMENT);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalStrut(GAP_TOP));
        add(wordLabel);
        add(Box.createVerticalStrut(GAP_BETWEEN));
        add(progressLabel);
        add(Box.createVerticalStrut(GAP_BELOW));
        add(nav);

        // Actions with braces and descriptive parameter names
        prev.addActionListener(event -> {
            navController.handlePrevRequest(
                    viewModel.getState().getPagenumber(),
                    viewModel.getState().getTotalpage());
        });

        next.addActionListener(event -> {
            navController.handleNextRequest(
                    viewModel.getState().getPagenumber(),
                    viewModel.getState().getTotalpage());
        });

        flip.addActionListener(event -> {
            wordDetailController.execute(viewModel.getState().getPagenumber());
        });

        finish.addActionListener(event -> {
            finishController.execute(viewModel.getState().getUsername());
        });
    }

    /**
     * Applies view-model updates to the UI.
     *
     * @param evt the property change event; only the {@code "state"} key is handled
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final StudySessionState state = (StudySessionState) evt.getNewValue();
            wordLabel.setText(state.getWord());
            progressLabel.setText(state.getPagenumber() + " / " + state.getTotalpage());
            prev.setEnabled(!state.isReachfirst());
            flip.setEnabled(!state.getWord().equals("Welcome"));
            next.setEnabled(!state.isReachlast());
            finish.setEnabled(state.isReachlast());
        }
    }

    /**
     * Returns the logical name of this view.
     *
     * @return the constant view name
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Injects the controller that handles previous/next navigation.
     *
     * @param controller the navigation controller
     */
    public void setStudySessionController(final StudySessionController controller) {
        this.navController = controller;
    }

    /**
     * Injects the controller that finalizes the check-in/study flow.
     *
     * @param controller the finish controller
     */
    public void setFinishCheckInController(final FinishController controller) {
        this.finishController = controller;
    }

    /**
     * Injects the controller that opens the word detail.
     *
     * @param controller the word-detail controller
     */
    public void setWordDetailController(final WordDetailController controller) {
        this.wordDetailController = controller;
    }
}
