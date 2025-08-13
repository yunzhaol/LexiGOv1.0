package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import interface_adapter.change_password.ChangePwState;
import interface_adapter.change_password.ChangeViewModel;
import interface_adapter.change_password.MakePasswordChange.MakePasswordChangeController;

/**
 * Change-Password UI — shows the current user and accepts a new password.
 *
 * <p>
 * If the user has a security question configured, clicking <em>Submit</em> pops
 * up a dialog to collect the answer. On success the controller is invoked; on
 * cancel the operation is aborted.
 * </p>
 */
public class ChangePasswordView extends JPanel implements ActionListener, PropertyChangeListener {

    /** Logical view name for routing/navigation. */
    public static final String VIEW_NAME = "change password";

    // -------------------- UI constants (remove magic numbers) --------------------

    /** Default padding applied to the view border (pixels). */
    private static final int BORDER_PADDING = 20;

    /** Vertical spacer before the title (pixels). */
    private static final int TITLE_TOP_SPACER = 43;

    /** Vertical spacer between rows (pixels). */
    private static final int ROW_SPACER = 20;

    /** Columns for the password field. */
    private static final int PASSWORD_FIELD_COLUMNS = 15;

    /** Title font size (points). */
    private static final float TITLE_FONT_SIZE = 18f;

    // -------------------- Dependencies --------------------

    private final ChangeViewModel viewModel;
    private MakePasswordChangeController controller;

    // -------------------- Swing widgets --------------------

    private final JLabel currentUserLbl = new JLabel();
    private final JPasswordField newPwFld = new JPasswordField(PASSWORD_FIELD_COLUMNS);
    private final JLabel newPwErrLbl = new JLabel();
    private final JButton submitBtn = new JButton("Submit");

    /** Whether a security-question answer is required (comes from ViewModel). */
    private boolean needSecurityAnswer;

    /**
     * Creates the view and registers listeners on the supplied view model.
     *
     * @param viewModel the view model to bind to; must not be {@code null}
     */
    public ChangePasswordView(final ChangeViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);
        buildUi();
        syncFromState(viewModel.getState());
    }

    // -------------------- UI construction --------------------

    /**
     * Builds and lays out Swing components.
     */
    private void buildUi() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING));

        final JLabel title = new JLabel("Change Password", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(TITLE_FONT_SIZE));
        add(Box.createVerticalStrut(TITLE_TOP_SPACER));
        add(title);

        // Current user row
        final JPanel userRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userRow.add(new JLabel("Current User: "));
        userRow.add(currentUserLbl);
        add(Box.createVerticalStrut(ROW_SPACER));
        add(userRow);

        // New password row
        add(makeRow("New Password", newPwFld, newPwErrLbl));

        // Submit button
        final JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnRow.add(submitBtn);
        add(btnRow);

        submitBtn.addActionListener(this);

        // Bind new password field to ViewModel
        docBind(newPwFld, ChangePwState::setPassword);
    }

    /**
     * Creates a labeled row with an optional error label.
     *
     * @param label      the text label for the field
     * @param field      the input component
     * @param errorLabel the error label (may be {@code null})
     * @return a populated panel containing the row
     */
    private JPanel makeRow(final String label, final JComponent field, final JLabel errorLabel) {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(new JLabel(label + ":"));
        panel.add(field);
        if (errorLabel != null) {
            errorLabel.setForeground(Color.RED);
            panel.add(errorLabel);
        }
        return panel;
    }

    // -------------------- Document binding helper --------------------

    /**
     * Binds a text component to a state mutator and updates the view model when the
     * document changes.
     *
     * @param comp    the text component to observe
     * @param mutator a state mutator to apply the current text to
     */
    private void docBind(final JTextComponent comp, final Mutator mutator) {
        comp.getDocument().addDocumentListener(new DocumentListener() {

            private void sync() {
                final ChangePwState state = viewModel.getState();
                final String value;
                if (comp instanceof JPasswordField) {
                    value = new String(((JPasswordField) comp).getPassword());
                }
                else {
                    value = comp.getText();
                }
                mutator.apply(state, value);
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
    }

    // -------------------- Actions --------------------

    /**
     * Handles button clicks within this view.
     *
     * @param event the action event
     */
    @Override
    public void actionPerformed(final ActionEvent event) {
        if (event.getSource() != submitBtn || controller == null) {
            return;
        }

        final ChangePwState state = viewModel.getState();
        final String newPwd = state.getPassword();
        final String user   = state.getUsername();

        // Mirror state flag
        needSecurityAnswer = state.isVerification();

        String answer = null;
        if (needSecurityAnswer) {
            final JTextField answerField = new JTextField();
            final Object[] content = {
                    new JLabel(state.getSecurityQuestion()),
                    answerField
            };
            final Object[] options = {"Submit", "Cancel"};
            final int choice = JOptionPane.showOptionDialog(
                    this,
                    content,
                    "Enter Security Question Answer",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0) {
                final String typed = answerField.getText().trim();
                if (!typed.isEmpty()) {
                    answer = typed;
                }
            }
        }

        controller.execute(user, newPwd, answer);

        // Re-read in case the state was updated by the controller/presenter
        final String err = viewModel.getState().getChangeError();
        if (err != null && !err.isBlank()) {
            JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // -------------------- ViewModel sync --------------------

    /**
     * Receives view-model updates and refreshes the UI.
     *
     * @param evt the property change event (only the {@code "state"} key is handled)
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            syncFromState((ChangePwState) evt.getNewValue());
        }
    }

    /**
     * Applies the given state to the UI controls.
     *
     * @param state the {@link ChangePwState} to read values from
     */
    private void syncFromState(final ChangePwState state) {
        currentUserLbl.setText(state.getUsername());
        newPwFld.setText(state.getPassword());
        // newPwErrLbl.setText(state.getChangeError());
        this.needSecurityAnswer = state.isVerification();
    }

    // -------------------- Wiring --------------------

    /**
     * Injects the controller used to submit the password change.
     *
     * @param controller the controller to invoke on submit
     */
    public void setController(final MakePasswordChangeController controller) {
        this.controller = controller;
    }

    /**
     * Returns the logical name of this view for routing.
     *
     * @return a constant view name
     */
    public String getViewName() {
        return VIEW_NAME;
    }

    // -------------------- Nested types (must appear last: InnerTypeLast) --------------------

    /**
     * Functional interface that mutates a {@link ChangePwState} with a new value.
     */
    private interface Mutator {
        void apply(ChangePwState state, String value);
    }
}
