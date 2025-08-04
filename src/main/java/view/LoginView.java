package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

/**
 * The view shown when a user logs into the program.
 *
 * <p>
 * This class wires UI controls to the {@link LoginViewModel} and forwards user
 * actions to a {@link LoginController}. It performs only presentation and event
 * binding; no application logic is implemented here.
 * </p>
 */
public class LoginView extends JPanel implements ActionListener, PropertyChangeListener {

    // ---- Constants (eliminate magic numbers) ----
    private static final int TEXT_COLUMNS = 15;
    private static final float TITLE_FONT_SIZE = 24f;
    private static final float LABEL_FONT_SIZE = 16f;

    private static final int OUTER_PADDING = 10;
    private static final int LEFT_PANEL_WIDTH = 286;

    private static final int LEFT_IMAGE_WIDTH = 200;

    private static final int ASCII_FONT_SIZE = 12;
    private static final int ASCII_BORDER_V = 6;
    private static final int ASCII_BORDER_H = 2;
    private static final int GAP_AFTER_IMAGE = 27;
    private static final int GAP_LARGE = 50;
    private static final int GAP_SMALL = 10;

    /** Brand-like blue used by the ASCII art. */
    private static final Color BRAND_BLUE = new Color(100, 149, 237);

    private final String viewName = "log in";
    private final LoginViewModel loginViewModel;

    private final JTextField usernameInputField = new JTextField(TEXT_COLUMNS);
    private final JLabel usernameErrorField = new JLabel();

    private final JPasswordField passwordInputField = new JPasswordField(TEXT_COLUMNS);
    private final JLabel passwordErrorField = new JLabel();

    private final JButton toSignUp;
    private final JButton logIn;
    private final JButton cancel;
    private LoginController loginController;

    /**
     * Creates the view and initializes UI bindings.
     *
     * @param loginViewModel the view model to observe and update
     */
    public LoginView(final LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addPropertyChangeListener(this);

        final JLabel title = createTitle();
        final LabelTextPanel usernameInfo = createUsernameRow();
        final LabelTextPanel passwordInfo = createPasswordRow();

        // Initialize final buttons exactly once (fix: avoid re-assignment later)
        this.toSignUp = new JButton("to sign up");
        this.logIn = new JButton("log in");
        this.cancel = new JButton("cancel");

        final JPanel buttons = buildButtons();

        bindDocumentListeners();

        setLayout(new BorderLayout());

        final JPanel leftPanel = buildLeftPanel();
        final JPanel centerPanel = buildCenterPanel(title, usernameInfo, passwordInfo, buttons);

        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
    }

    // -------------------- UI builders --------------------

    private JLabel createTitle() {
        final JLabel title = new JLabel("Login LexiGo");
        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        return title;
    }

    private LabelTextPanel createUsernameRow() {
        final JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(usernameLabel.getFont().deriveFont(LABEL_FONT_SIZE));
        return new LabelTextPanel(usernameLabel, usernameInputField);
    }

    private LabelTextPanel createPasswordRow() {
        final JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(passwordLabel.getFont().deriveFont(LABEL_FONT_SIZE));
        return new LabelTextPanel(passwordLabel, passwordInputField);
    }

    /**
     * Creates the button bar and wires button actions.
     *
     * @return a panel containing sign-up, log-in, and cancel buttons
     */
    private JPanel buildButtons() {
        final JPanel buttons = new JPanel();
        buttons.add(toSignUp);
        buttons.add(logIn);
        buttons.add(cancel);

        toSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                loginController.switchToSignUpView();
            }
        });

        logIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                if (evt.getSource().equals(logIn)) {
                    final LoginState currentState = loginViewModel.getState();
                    loginController.execute(currentState.getUsername(), currentState.getPassword());
                }
            }
        });

        cancel.addActionListener(this);
        return buttons;
    }

    private void bindDocumentListeners() {
        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                loginViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                documentListenerHelper();
            }
        });

        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
                loginViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                documentListenerHelper();
            }
        });
    }

    /**
     * Builds the left-side panel containing ASCII art and an image.
     *
     * <p><strong>Note:</strong> The ASCII banner text is preserved verbatim to
     * avoid altering its formatting.</p>
     *
     * @return the fully configured left panel
     */
    private JPanel buildLeftPanel() {
        final JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(
                BorderFactory.createEmptyBorder(OUTER_PADDING, OUTER_PADDING, OUTER_PADDING, OUTER_PADDING));
        leftPanel.setPreferredSize(new Dimension(LEFT_PANEL_WIDTH, 0));

        final ImageIcon leftIcon;
        final URL url = getClass().getResource("/assets/login-left.png");
        if (url != null) {
            leftIcon = new ImageIcon(url);
        }
        else {
            leftIcon = new ImageIcon("src/assets/login-left.png");
        }

        final Image scaled = leftIcon.getImage().getScaledInstance(LEFT_IMAGE_WIDTH, -1, Image.SCALE_SMOOTH);
        final JLabel leftImageLabel = new JLabel(new ImageIcon(scaled));
        leftImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final String ascii =
                "\n\n\n\n     _         _   _                \n"
                        + "    / \\  _   _| |_| |__   ___  _ __ \n"
                        + "   / _ \\| | | | __| '_ \\ / _ \\| '__|\n"
                        + "  / ___ | |_| | |_| | | | (_) | |   \n"
                        + " /_/   \\_\\__,_|\\__|_| |_|\\___/|_|   \n"
                        + "\n"
                        + "   _____                               \n"
                        + "  |_   _|   ___    __ _   _ __ ___    _\n"
                        + "    | |    / _ \\  / _` | | '_ ` _ \\  (_)\n"
                        + "    | |   |  __/ | (_| | | | | | | |  _ \n"
                        + "    |_|    \\___|  \\__,_| |_| |_| |_| (_)\n";

        final JTextArea asciiArea = new JTextArea(ascii);
        asciiArea.setEditable(false);
        asciiArea.setFont(new Font("Monospaced", Font.BOLD, ASCII_FONT_SIZE));
        asciiArea.setBackground(getBackground());
        asciiArea.setForeground(BRAND_BLUE);
        asciiArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        asciiArea.setLineWrap(false);
        asciiArea.setWrapStyleWord(false);
        asciiArea.setBorder(BorderFactory.createEmptyBorder(
                ASCII_BORDER_V, ASCII_BORDER_H, ASCII_BORDER_V, ASCII_BORDER_H));

        leftPanel.add(asciiArea);
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(leftImageLabel);
        leftPanel.add(Box.createVerticalStrut(GAP_AFTER_IMAGE));
        return leftPanel;
    }

    private JPanel buildCenterPanel(final JLabel title,
                                    final JComponent usernameInfo,
                                    final JComponent passwordInfo,
                                    final JPanel buttons) {
        final JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(
                OUTER_PADDING, OUTER_PADDING * 2, OUTER_PADDING, OUTER_PADDING * 2));

        centerPanel.add(Box.createVerticalStrut(GAP_LARGE));
        centerPanel.add(title);
        centerPanel.add(Box.createVerticalStrut(GAP_LARGE));
        centerPanel.add(usernameInfo);
        centerPanel.add(usernameErrorField);
        centerPanel.add(passwordInfo);
        centerPanel.add(Box.createVerticalStrut(GAP_SMALL));
        centerPanel.add(buttons);
        centerPanel.add(Box.createVerticalGlue());
        return centerPanel;
    }

    // -------------------- Listeners --------------------

    /**
     * Reacts to button clicks.
     *
     * @param evt the action event
     */
    @Override
    public void actionPerformed(final ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    /**
     * Listens for changes to the {@link LoginViewModel} and refreshes the view.
     *
     * @param evt the property change event
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final LoginState state = (LoginState) evt.getNewValue();
        setFields(state);
        usernameErrorField.setText(state.getLoginError());
    }

    /**
     * Copies state from the view model to the form fields.
     *
     * @param state the source state
     */
    private void setFields(final LoginState state) {
        usernameInputField.setText(state.getUsername());
        passwordInputField.setText(state.getPassword());
    }

    /**
     * Returns the logical name of this view.
     *
     * @return the view name
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Injects the {@link LoginController} to handle user actions.
     *
     * @param loginController the controller to use
     */
    public void setLoginController(final LoginController loginController) {
        this.loginController = loginController;
    }
}
