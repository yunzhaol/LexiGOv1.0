package view;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the Signup Use Case.
 */
public class SignupView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "sign up";

    private final SignupViewModel signupViewModel;
    private final JTextField usernameInputField = new JTextField(15);
    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JPasswordField repeatPasswordInputField = new JPasswordField(15);
    private final JTextField securityQuestionInputField = new JTextField(15);
    private final JTextField securityAnswerInputField = new JTextField(15);
    private SignupController signupController;

    private final JButton signUp;
    private final JButton cancel;
    private final JButton toLogin;

    public SignupView(SignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
        signupViewModel.addPropertyChangeListener(this);

        //add(Box.createVerticalStrut(60));
        // Create logo
        String logo =
                "\n\n\n\n\n\n\n   ____   ____     ____   ____     ___    _____ \n" +
                        "  / ___| / ___|   / ___| |___ \\   / _ \\  |___  |\n" +
                        " | |     \\___ \\  | |       __) | | | | |    / / \n" +
                        " | |___   ___) | | |___   / __/  | |_| |   / /  \n" +
                        "  \\____| |____/   \\____| |_____|  \\___/   /_/   \n" +
                        "\n" +
                        "               L E X I G O   T e a m";

        JTextArea logoArea = new JTextArea(logo);
        logoArea.setEditable(false);
        logoArea.setFont(new Font("Monospaced", Font.BOLD, 12));
        logoArea.setBackground(getBackground());
        logoArea.setForeground(Color.CYAN);
        logoArea.setMargin(new Insets(10, 10, 10, 10));

        // Create form panel (original UI)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        final JLabel title = new JLabel(SignupViewModel.TITLE_LABEL);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.USERNAME_LABEL), usernameInputField);
        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.PASSWORD_LABEL), passwordInputField);
        final LabelTextPanel repeatPasswordInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.REPEAT_PASSWORD_LABEL), repeatPasswordInputField);

        final LabelTextPanel securityQuestionInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.SECURITY_QUESTION_LABEL), securityQuestionInputField
        );
        final LabelTextPanel securityAnswerInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.SECURITY_ANSWER_LABEL), securityAnswerInputField
        );

        final JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        toLogin = new JButton(SignupViewModel.TO_LOGIN_BUTTON_LABEL);
        buttons.add(toLogin);
        signUp = new JButton(SignupViewModel.SIGNUP_BUTTON_LABEL);
        buttons.add(signUp);
        cancel = new JButton(SignupViewModel.CANCEL_BUTTON_LABEL);
        buttons.add(cancel);

        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(usernameInfo);
        formPanel.add(Box.createVerticalStrut(1));
        formPanel.add(passwordInfo);
        formPanel.add(Box.createVerticalStrut(1));
        formPanel.add(repeatPasswordInfo);
        formPanel.add(Box.createVerticalStrut(1));
        formPanel.add(securityQuestionInfo);
        formPanel.add(Box.createVerticalStrut(1));
        formPanel.add(securityAnswerInfo);
        formPanel.add(Box.createVerticalStrut(1));
        formPanel.add(buttons);

        signUp.addActionListener(e -> {
            final SignupState currentState = signupViewModel.getState();
            if (currentState.getSecurityAnswer().isBlank() || currentState.getSecurityQuestion().isBlank()) {
                signupController.signup(currentState.getUsername(),
                        currentState.getPassword(),
                        currentState.getRepeatPassword());
            } else {
                signupController.signupWithSecurity(
                        currentState.getUsername(),
                        currentState.getPassword(),
                        currentState.getRepeatPassword(),
                        currentState.getSecurityQuestion(),
                        currentState.getSecurityAnswer()
                );
            }
        });

        toLogin.addActionListener(e -> signupController.switchToLoginView());
        cancel.addActionListener(this);

        addUsernameListener();
        addPasswordListener();
        addRepeatPasswordListener();
        addSecurityQuestionListener();
        addSecurityAnswerListener();

        // Set layout with logo
        this.setLayout(new BorderLayout());
        this.add(logoArea, BorderLayout.WEST);
        this.add(formPanel, BorderLayout.CENTER);
    }

    private void addUsernameListener() {
        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                signupViewModel.setState(currentState);
            }
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }
        });
    }

    private void addPasswordListener() {
        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
                signupViewModel.setState(currentState);
            }
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }
        });
    }

    private void addRepeatPasswordListener() {
        repeatPasswordInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setRepeatPassword(new String(repeatPasswordInputField.getPassword()));
                signupViewModel.setState(currentState);
            }
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }
        });
    }

    private void addSecurityQuestionListener() {
        securityQuestionInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setSecurityQuestion(securityQuestionInputField.getText());
                signupViewModel.setState(currentState);
            }
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }
        });
    }

    private void addSecurityAnswerListener() {
        securityAnswerInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setSecurityAnswer(securityAnswerInputField.getText());
                signupViewModel.setState(currentState);
            }
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }
        });
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Cancel not implemented yet.");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
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

    public String getViewName() {
        return viewName;
    }

    public void setSignupController(SignupController controller) {
        this.signupController = controller;
    }
}
