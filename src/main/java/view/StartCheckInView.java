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
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.start_checkin.StartCheckInController;
import interface_adapter.start_checkin.StartCheckInState;
import interface_adapter.start_checkin.StartCheckInViewModel;

/**
 * Screen that lets the user specify how many new words to study today.
 *
 * <p>
 * This view binds Swing controls to {@link StartCheckInViewModel} and forwards actions
 * to a {@link StartCheckInController}. It contains no business logic.
 * </p>
 */
public class StartCheckInView extends JPanel implements PropertyChangeListener {

    // ----- Constants (remove magic numbers) -----

    /** Title font size (pt). */
    private static final float TITLE_FONT_SIZE = 24f;

    /** Field font size (pt). */
    private static final float FIELD_FONT_SIZE = 17f;

    /** Top spacing before the title (px). */
    private static final int TOP_GAP = 63;

    /** Gap between title and form (px). */
    private static final int MID_GAP = 30;

    /** Text field column count. */
    private static final int NUMBER_COLUMNS = 4;

    // ----- State & collaborators -----

    private final String viewName = "start checkin";
    private final StartCheckInViewModel viewModel;

    private final JTextField numberField = new JTextField(NUMBER_COLUMNS);
    private final JLabel numberError = new JLabel();

    private final JButton startButton = new JButton("Start ->");

    private StartCheckInController controller;

    /**
     * Creates the view and wires it to the supplied view model.
     *
     * @param viewModel the view model to observe
     */
    public StartCheckInView(final StartCheckInViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        // ------------------------- build UI -------------------------
        final JLabel title = new JLabel("Daily Check-In");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));

        final LabelTextPanel numberInfo = new LabelTextPanel(new JLabel("Words to learn"), numberField);
        numberInfo.setFont(title.getFont().deriveFont(FIELD_FONT_SIZE));
        startButton.setFont(title.getFont().deriveFont(FIELD_FONT_SIZE));

        final JPanel buttons = new JPanel();
        buttons.add(startButton);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalStrut(TOP_GAP));
        add(title);
        add(Box.createVerticalStrut(MID_GAP));
        add(numberInfo);
        add(numberError);
        add(buttons);

        // ------------------------- event wiring -------------------------
        numberField.getDocument().addDocumentListener(new DocumentListener() {

            private void sync() {
                final StartCheckInState state = viewModel.getState();
                state.setNumberWords(numberField.getText());
                viewModel.setState(state);
            }

            @Override
            public void insertUpdate(final DocumentEvent event) {
                sync();
            }

            @Override
            public void removeUpdate(final DocumentEvent event) {
                sync();
            }

            @Override
            public void changedUpdate(final DocumentEvent event) {
                sync();
            }
        });

        startButton.addActionListener(event -> {
            final StartCheckInState state = viewModel.getState();
            controller.execute(state.getUsername(), state.getNumberWords());
        });
    }

    /**
     * Receives state updates and refreshes the UI.
     *
     * @param evt the property change event; only the {@code "state"} key is handled
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final StartCheckInState state = (StartCheckInState) evt.getNewValue();
            numberError.setText(state.getInputNumberError());
            numberField.setText(state.getNumberWords());
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
     * Injects the controller used to start the check-in flow.
     *
     * @param controller the controller instance
     */
    public void setController(final StartCheckInController controller) {
        this.controller = controller;
    }
}
