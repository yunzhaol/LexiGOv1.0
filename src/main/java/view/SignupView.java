package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;

/**
 * The View for the Signup Use Case.
 *
 * <p>
 * This view binds Swing controls to {@link SignupViewModel} and forwards user
 * actions to a {@link SignupController}. Business logic is not implemented
 * here; only presentation and event wiring.
 * </p>
 */
public class SignupView extends JPanel implements ActionListener, PropertyChangeListener {

    // -------------------- Constants (remove magic numbers) --------------------

    /** Title text shown above the form. */
    public static final String TITLE_LABEL = "Create Your Account";

    /** Number of columns for all text fields. */
    private static final int TEXT_COLUMNS = 15;

    /** Monospace font size for the ASCII logo. */
    private static final int LOGO_FONT_SIZE = 12;

    /** Uniform margin used around the ASCII logo. */
    private static final int LOGO_MARGIN = 10;

    /** Vertical spacing used between stacked form rows. */
    private static final int GAP_V_LARGE = 20;

    /** Very small vertical spacing used between label/field rows. */
    private static final int GAP_V_SMALL = 1;

    /** Horizontal gap for the button row. */
    private static final int BUTTON_HGAP = 20;

    /** Title font size. */
    private static final float TITLE_FONT_SIZE = 24f;

    // -------------------- State & collaborators --------------------

    private final String viewName = "sign up";

    private final SignupViewModel signupViewModel;

    private final JTextField usernameInputField = new JTextField(TEXT_COLUMNS);
    private final JPasswordField passwordInputField = new JPasswordField(TEXT_COLUMNS);
    private final JPasswordField repeatPasswordInputField = new JPasswordField(TEXT_COLUMNS);
    private final JTextField securityQuestionInputField = new JTextField(TEXT_COLUMNS);
    private final JTextField securityAnswerInputField = new JTextField(TEXT_COLUMNS);

    private SignupController signupController;

    private JButton signUp;
    private JButton cancel;
    private JButton toLogin;

    /**
     * Creates the signup view and initializes all UI bindings.
     *
     * @param signupViewModel the view model to observe and update
     */
    public SignupView(final SignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
        signupViewModel.addPropertyChangeListener(this);

        final JTextArea logoArea = buildLogoArea();
        final JPanel formPanel = buildFormPanel();

        addButtonListeners();
        addInputFieldListeners();

        setLayout(new BorderLayout());
        add(logoArea, BorderLayout.WEST);
        add(formPanel, BorderLayout.CENTER);
    }

    /**
     * Builds and returns the ASCII logo area for the UI.
     *
     * @return JTextArea with logo text
     */
    private JTextArea buildLogoArea() {
        final String logo = buildLogoText();

        final JTextArea logoArea = new JTextArea(logo);
        logoArea.setEditable(false);
        logoArea.setFont(new Font("Monospaced", Font.BOLD, LOGO_FONT_SIZE));
        logoArea.setBackground(getBackground());
        logoArea.setForeground(Color.blue);
        logoArea.setMargin(new Insets(LOGO_MARGIN, LOGO_MARGIN, LOGO_MARGIN, LOGO_MARGIN));

        return logoArea;
    }

    /**
     * Provides ASCII logo text as a string.
     *
     * @return String containing ASCII logo
     */
    private String buildLogoText() {
        return "\n\n\n\n\n\n\n\n   ____   ____     ____   ____     ___    _____ \n"
                + "  / ___| / ___|   / ___| |___ \\   / _ \\  |___  |\n"
                + " | |     \\___ \\  | |       __) | | | | |    / / \n"
                + " | |___   ___) | | |___   / __/  | |_| |   / /  \n"
                + "  \\____| |____/   \\____| |_____|  \\___/   /_/   \n"
                + "\n"
                + "               L E X I G O   T e a m";
    }

    /**
     * Constructs and returns the full signup form panel.
     *
     * @return JPanel with input fields and buttons
     */
    private JPanel buildFormPanel() {
        final JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        addFormComponents(formPanel);

        return formPanel;
    }

    /**
     * Adds all components to the form panel.
     *
     * @param formPanel the panel to populate
     */
    private void addFormComponents(final JPanel formPanel) {
        final JLabel title = new JLabel(TITLE_LABEL);
        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.USERNAME_LABEL), usernameInputField);
        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.PASSWORD_LABEL), passwordInputField);
        final LabelTextPanel repeatPasswordInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.REPEAT_PASSWORD_LABEL), repeatPasswordInputField);
        final LabelTextPanel securityQuestionInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.SECURITY_QUESTION_LABEL), securityQuestionInputField);
        final LabelTextPanel securityAnswerInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.SECURITY_ANSWER_LABEL), securityAnswerInputField);

        final JPanel buttons = buildButtonPanel();

        formPanel.add(Box.createVerticalStrut(GAP_V_LARGE));
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(GAP_V_LARGE));
        formPanel.add(usernameInfo);
        formPanel.add(Box.createVerticalStrut(GAP_V_SMALL));
        formPanel.add(passwordInfo);
        formPanel.add(Box.createVerticalStrut(GAP_V_SMALL));
        formPanel.add(repeatPasswordInfo);
        formPanel.add(Box.createVerticalStrut(GAP_V_SMALL));
        formPanel.add(securityQuestionInfo);
        formPanel.add(Box.createVerticalStrut(GAP_V_SMALL));
        formPanel.add(securityAnswerInfo);
        formPanel.add(Box.createVerticalStrut(GAP_V_SMALL));
        formPanel.add(buttons);
    }

    /**
     * Builds and returns the panel containing all action buttons.
     *
     * @return JPanel with signup, login, and cancel buttons
     */
    private JPanel buildButtonPanel() {
        final JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, BUTTON_HGAP, 0));
        toLogin = new JButton(SignupViewModel.TO_LOGIN_BUTTON_LABEL);
        signUp = new JButton(SignupViewModel.SIGNUP_BUTTON_LABEL);
        cancel = new JButton(SignupViewModel.CANCEL_BUTTON_LABEL);

        buttons.add(toLogin);
        buttons.add(signUp);

        return buttons;
    }

    /**
     * Adds action listeners to the main buttons.
     */
    private void addButtonListeners() {
        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                handleSignUp();
            }
        });

        toLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                signupController.switchToLoginView();
            }
        });

        cancel.addActionListener(this);
    }

    /**
     * Adds listeners to all form input fields.
     */
    private void addInputFieldListeners() {
        addUsernameListener();
        addPasswordListener();
        addRepeatPasswordListener();
        addSecurityQuestionListener();
        addSecurityAnswerListener();
    }

    /**
     * Handles the Sign Up button click; dispatches to the correct controller method.
     */
    private void handleSignUp() {
        final SignupState currentState = signupViewModel.getState();
        if (currentState.getSecurityAnswer().isBlank() || currentState.getSecurityQuestion().isBlank()) {
            signupController.signup(
                    currentState.getUsername(),
                    currentState.getPassword(),
                    currentState.getRepeatPassword());
        }
        else {
            signupController.signupWithSecurity(
                    currentState.getUsername(),
                    currentState.getPassword(),
                    currentState.getRepeatPassword(),
                    currentState.getSecurityQuestion(),
                    currentState.getSecurityAnswer());
        }
    }

    /** Wires the username text field to the view model. */
    private void addUsernameListener() {
        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void update() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                signupViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(final DocumentEvent event) {
                update();
            }

            @Override
            public void removeUpdate(final DocumentEvent event) {
                update();
            }

            @Override
            public void changedUpdate(final DocumentEvent event) {
                update();
            }
        });
    }

    /** Wires the password field to the view model. */
    private void addPasswordListener() {
        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void update() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
                signupViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(final DocumentEvent event) {
                update();
            }

            @Override
            public void removeUpdate(final DocumentEvent event) {
                update();
            }

            @Override
            public void changedUpdate(final DocumentEvent event) {
                update();
            }
        });
    }

    /** Wires the repeat-password field to the view model. */
    private void addRepeatPasswordListener() {
        repeatPasswordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void update() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setRepeatPassword(new String(repeatPasswordInputField.getPassword()));
                signupViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(final DocumentEvent event) {
                update();
            }

            @Override
            public void removeUpdate(final DocumentEvent event) {
                update();
            }

            @Override
            public void changedUpdate(final DocumentEvent event) {
                update();
            }
        });
    }

    /** Wires the security-question field to the view model. */
    private void addSecurityQuestionListener() {
        securityQuestionInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void update() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setSecurityQuestion(securityQuestionInputField.getText());
                signupViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(final DocumentEvent event) {
                update();
            }

            @Override
            public void removeUpdate(final DocumentEvent event) {
                update();
            }

            @Override
            public void changedUpdate(final DocumentEvent event) {
                update();
            }
        });
    }

    /** Wires the security-answer field to the view model. */
    private void addSecurityAnswerListener() {
        securityAnswerInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void update() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setSecurityAnswer(securityAnswerInputField.getText());
                signupViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(final DocumentEvent event) {
                update();
            }

            @Override
            public void removeUpdate(final DocumentEvent event) {
                update();
            }

            @Override
            public void changedUpdate(final DocumentEvent event) {
                update();
            }
        });
    }

    /**
     * Reacts to the Cancel button.
     *
     * @param evt the action event
     */
    @Override
    public void actionPerformed(final ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Cancel not implemented yet.");
    }

    /**
     * Receives state changes from {@link SignupViewModel} and updates the form.
     *
     * @param evt the property change event
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final SignupState state = (SignupState) evt.getNewValue();
        usernameInputField.setText(state.getUsername());
        passwordInputField.setText(state.getPassword());
        repeatPasswordInputField.setText(state.getRepeatPassword());
        securityQuestionInputField.setText(state.getSecurityQuestion());
        securityAnswerInputField.setText(state.getSecurityAnswer());
        if (state.getUsernameError() != null) {
            JOptionPane.showMessageDialog(this, state.getUsernameError());
        }
    }

    /**
     * Returns the logical name of this view.
     *
     * @return the view name constant
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Injects the controller responsible for sign-up actions.
     *
     * @param controller the controller instance to use
     */
    public void setSignupController(final SignupController controller) {
        this.signupController = controller;
    }
}
