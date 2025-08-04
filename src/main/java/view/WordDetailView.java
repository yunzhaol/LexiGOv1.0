package view;

import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import interface_adapter.studysession.word_detail.WordDetailController;
import interface_adapter.studysession.word_detail.WordDetailState;
import interface_adapter.studysession.word_detail.WordDetailViewModel;

/**
 * Displays the translation and usage example for the currently selected word.
 *
 * <p>
 * The view listens to {@link WordDetailViewModel} for state updates and
 * refreshes the labels when the state changes. A back button allows switching
 * to the study-session view via {@link WordDetailController}.
 * </p>
 */
public class WordDetailView extends JPanel implements PropertyChangeListener {

    // ---- Constants (remove magic numbers) ----
    private static final float TITLE_FONT_SIZE = 18f;
    private static final float EXAMPLE_FONT_SIZE = 16f;
    private static final int GAP_TOP = 45;
    private static final int GAP_MID = 25;
    private static final int GAP_BOTTOM = 20;

    private static final String VIEW_NAME = "word detail";

    // ---- Collaborators ----
    private final WordDetailViewModel viewModel;
    private WordDetailController controller;

    // ---- Components ----
    private final JLabel transLabel = new JLabel("Translation", SwingConstants.CENTER);
    private final JLabel exampleLabel = new JLabel("Example", SwingConstants.CENTER);
    private final JButton flipBack = new JButton("Flip back");

    /**
     * Creates the word-detail view and wires it to the given view model.
     *
     * @param viewModel the view model to observe
     */
    public WordDetailView(final WordDetailViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addPropertyChangeListener(this);

        transLabel.setFont(transLabel.getFont().deriveFont(Font.PLAIN, TITLE_FONT_SIZE));
        exampleLabel.setFont(exampleLabel.getFont().deriveFont(Font.ITALIC, EXAMPLE_FONT_SIZE));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        transLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        exampleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(GAP_TOP));
        add(transLabel);
        add(Box.createVerticalStrut(GAP_MID));
        add(exampleLabel);
        add(Box.createVerticalStrut(GAP_BOTTOM));

        final JPanel nav = new JPanel();
        nav.add(flipBack);
        add(nav);

        flipBack.addActionListener(event -> {
            if (controller != null) {
                controller.switchToStudySessionView();
            }
        });
    }

    /**
     * Applies new state from the view model to the UI.
     *
     * @param evt the property change event; only the {@code "state"} key is handled
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final WordDetailState state = (WordDetailState) evt.getNewValue();
            transLabel.setText(state.getTranslation());

            final String html =
                    "<html><body style='text-align:center'>"
                            + state.getExample()
                            + "</body></html>";
            exampleLabel.setText(html);
        }
    }

    /**
     * Returns the logical name of this view.
     *
     * @return the constant view name
     */
    public String getViewName() {
        return VIEW_NAME;
    }

    /**
     * Injects the controller used to navigate back to the study session.
     *
     * @param controllerParam the controller instance
     */
    public void setWordDetailController(final WordDetailController controllerParam) {
        this.controller = controllerParam;
    }
}
