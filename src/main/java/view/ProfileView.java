package view;

import entity.Language;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;
import interface_adapter.profile.profile_set.ProfileSetController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.BiConsumer;

/**
 * 用户资料页面：用户名只读；显示当前语言，并可下拉选择新的语言。
 */
public class ProfileView extends JPanel implements PropertyChangeListener, ActionListener {

    /* ── 依赖 ───────────────────────────── */
    private final ProfileViewModel vm;
    private BiConsumer<String, Language> onSaveCallback;  // (username, newLang) -> void

    /* ── Swing 组件 ─────────────────────── */
    private final JLabel      title          = new JLabel("Profile", SwingConstants.CENTER);
    private final JTextField  usernameFld    = new JTextField(15);
    private final JLabel      currentLangVal = new JLabel();               // 当前语言展示
    private final JComboBox<Language> langCombo = new JComboBox<>();       // 新语言选择
    private final JButton     saveBtn        = new JButton("Save");
    private final JLabel      errorLabel     = new JLabel();

    public static final String VIEW_NAME = "profile";

    private final ProfileSetController controller;

    /* ── 构造 ───────────────────────────── */
    public ProfileView(ProfileViewModel vm, ProfileSetController controller) {
        this.controller = controller;
        this.vm = vm;
        this.vm.addPropertyChangeListener(this);

        /* 渲染器：显示 Language.displayName() */
        langCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                String text;
                if (value instanceof Language) {
                    text = ((Language) value).displayName();
                } else {
                    text = String.valueOf(value);
                }
                return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
            }
        });

        /* 布局 */
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        /* Username 行 */
        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        add(usernameFld, gbc);
        usernameFld.setEditable(false);

        /* Current Language 行 */
        gbc.gridy = 2; gbc.gridx = 0;
        add(new JLabel("Current Language:"), gbc);
        gbc.gridx = 1;
        add(currentLangVal, gbc);

        /* Preferred Language (select new) 行 */
        gbc.gridy = 3; gbc.gridx = 0;
        add(new JLabel("Preferred Language:"), gbc);
        gbc.gridx = 1;
        add(langCombo, gbc);

        /* Save 按钮 */
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(saveBtn, gbc);
        saveBtn.addActionListener(e -> {
            if (this.controller != null) {
                this.controller.execute(vm.getState().getUsername(),
                        vm.getState().getOldlanguage(),
                        (Language) langCombo.getSelectedItem());   // ★ 统一用 refresh
            }
        });

        /* 错误提示 */
        gbc.gridy = 5;
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(errorLabel, gbc);

        /* 初次同步 */
        updateFromState(vm.getState());
    }

    /* ── ActionListener —— Save 按钮 ─────────────────── */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveBtn && onSaveCallback != null) {
            String username = usernameFld.getText();
            Language selected = (Language) langCombo.getSelectedItem();
            onSaveCallback.accept(username, selected);
        }
    }

    /* ── PropertyChangeListener —— ViewModel -> View ─── */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            ProfileState s = (ProfileState) evt.getNewValue();
            SwingUtilities.invokeLater(() -> updateFromState(s));
        }
    }

    private void updateFromState(ProfileState s) {
        /* 用户名 */
        usernameFld.setText(s.getUsername());

        /* 语言列表 */
        if (s.getLanguages() != null) {
            DefaultComboBoxModel<Language> model = new DefaultComboBoxModel<>(s.getLanguages());
            langCombo.setModel(model);
        }

        /* 当前语言显示与下拉选中 */
        if (s.getOldlanguage() != null) {
            currentLangVal.setText(s.getOldlanguage().displayName());
            langCombo.setSelectedItem(s.getOldlanguage());
        } else {
            currentLangVal.setText("");
        }

        /* 错误提示 */
        errorLabel.setText(s.getLanguageError() != null ? s.getLanguageError() : "");
    }

    /* ── 对外 API ────────────────────────── */
    public void setOnSaveCallback(BiConsumer<String, Language> callback) {
        this.onSaveCallback = callback;
    }

    public String getViewName() { return VIEW_NAME; }
}
