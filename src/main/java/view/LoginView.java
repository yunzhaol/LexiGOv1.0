package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

/**
 * The View for when the user is logging into the program.
 */

/**
 * The View for when the user is logging into the program.
 */
public class LoginView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "log in";
    private final LoginViewModel loginViewModel;

    private final JTextField usernameInputField = new JTextField(15);
    private final JLabel usernameErrorField = new JLabel();

    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JLabel passwordErrorField = new JLabel();

    private final JButton toSignUp;
    private final JButton logIn;
    private final JButton cancel;
    private LoginController loginController;

    public LoginView(LoginViewModel loginViewModel) {

        this.loginViewModel = loginViewModel;
        this.loginViewModel.addPropertyChangeListener(this);

        // ====== 标题 ======
        final JLabel title = new JLabel("Login LexiGo");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ====== 表单行：用户名 / 密码 ======
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(usernameLabel.getFont().deriveFont(16f));
        final LabelTextPanel usernameInfo = new LabelTextPanel(usernameLabel, usernameInputField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(passwordLabel.getFont().deriveFont(16f));
        final LabelTextPanel passwordInfo = new LabelTextPanel(passwordLabel, passwordInputField);

        // ====== 底部按钮 ======
        final JPanel buttons = new JPanel();
        toSignUp = new JButton("to sign up");
        buttons.add(toSignUp);
        logIn = new JButton("log in");
        buttons.add(logIn);
        cancel = new JButton("cancel");
        buttons.add(cancel);

        // ====== 事件绑定（保持原有逻辑） ======
        toSignUp.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        loginController.switchToSignUpView();
                    }
                }
        );

        logIn.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(logIn)) {
                            final LoginState currentState = loginViewModel.getState();
                            loginController.execute(
                                    currentState.getUsername(),
                                    currentState.getPassword()
                            );
                        }
                    }
                }
        );

        cancel.addActionListener(this);

        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                loginViewModel.setState(currentState);
            }
            @Override public void insertUpdate(DocumentEvent e) { documentListenerHelper(); }
            @Override public void removeUpdate(DocumentEvent e) { documentListenerHelper(); }
            @Override public void changedUpdate(DocumentEvent e) { documentListenerHelper(); }
        });

        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
                loginViewModel.setState(currentState);
            }
            @Override public void insertUpdate(DocumentEvent e) { documentListenerHelper(); }
            @Override public void removeUpdate(DocumentEvent e) { documentListenerHelper(); }
            @Override public void changedUpdate(DocumentEvent e) { documentListenerHelper(); }
        });

        // ===============================
        //         布局：左图 + 中表单
        // ===============================
        setLayout(new BorderLayout());

        // --- 左侧面板：图像 + 字符画 ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.setPreferredSize(new Dimension(286, 0)); // 固定左侧宽度，按需调

        // 1) 加载左侧图像（把路径换成你的图片路径/资源路径）
        //    例：把你发的那张“头像组图”保存为 resources/assets/login-left.png
        //    然后用 getResource 加载，若用文件路径也可：new ImageIcon("src/assets/login-left.png")
        ImageIcon leftIcon;
        try {
            // 从 classpath 读取（推荐把图片放到 src/main/resources/assets/login-left.png）
            leftIcon = new ImageIcon(getClass().getResource("/assets/login-left.png"));
        } catch (Exception ex) {
            // 退化为文件路径（按需修改）
            leftIcon = new ImageIcon("src/assets/login-left.png");
        }
        // 可按需缩放（保持比例自行调整目标宽高）
        Image scaled = leftIcon.getImage().getScaledInstance(200, -1, Image.SCALE_SMOOTH);
        JLabel leftImageLabel = new JLabel(new ImageIcon(scaled));
        leftImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String ascii =
                // 第一个字符的图案（5行）
                "\n\n\n\n\n     _         _   _                \n" +
                        "    / \\  _   _| |_| |__   ___  _ __ \n" +
                        "   / _ \\| | | | __| '_ \\ / _ \\| '__|\n" +
                        "  / ___ | |_| | |_| | | | (_) | |   \n" +
                        " /_/   \\_\\__,_|\\__|_| |_|\\___/|_|   \n" +
                        "\n" +  // 空一行，分隔上下
                        // 第二个字符的图案（5行）
                        "   _____                               \n" +
                        "  |_   _|   ___    __ _   _ __ ___    _\n" +
                        "    | |    / _ \\  / _` | | '_ ` _ \\  (_)\n" +
                        "    | |   |  __/ | (_| | | | | | | |  _ \n" +
                        "    |_|    \\___|  \\__,_| |_| |_| |_| (_)\n";
        JTextArea asciiArea = new JTextArea(ascii);
        asciiArea.setEditable(false);

        asciiArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        asciiArea.setBackground(getBackground());
        asciiArea.setForeground(new Color(100, 149, 237)); //
        asciiArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        asciiArea.setLineWrap(false);
        asciiArea.setWrapStyleWord(false);
        asciiArea.setBorder(BorderFactory.createEmptyBorder(6, 2, 6, 2));

        leftPanel.add(asciiArea);
        leftPanel.add(Box.createVerticalGlue()); // 把内容“顶上去”，下面留白

        leftPanel.add(leftImageLabel);
        leftPanel.add(Box.createVerticalStrut(12));

        // --- 中间面板：原来的表单 ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(title);
        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(usernameInfo);
        centerPanel.add(usernameErrorField);
        centerPanel.add(passwordInfo);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(buttons);
        centerPanel.add(Box.createVerticalGlue());

        // --- 加到主容器 ---
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final LoginState state = (LoginState) evt.getNewValue();
        setFields(state);
        usernameErrorField.setText(state.getLoginError());
    }

    private void setFields(LoginState state) {
        usernameInputField.setText(state.getUsername());
        passwordInputField.setText(state.getPassword());
    }

    public String getViewName() {
        return viewName;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
