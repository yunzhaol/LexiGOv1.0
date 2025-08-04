package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.BiConsumer;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import entity.Language;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;
import interface_adapter.profile.profile_set.ProfileSetController;

/**
 * Profile screen that displays the username, current language, and allows the
 * user to choose a preferred language.
 *
 * <p>
 * The view listens to {@link ProfileViewModel} changes and refreshes UI
 * controls accordingly. It can also invoke a provided
 * {@link ProfileSetController} to persist changes.
 * </p>
 */
public class ProfileView extends JPanel implements PropertyChangeListener, ActionListener {

    // -------------------- Constants (remove magic numbers) --------------------

    /** Outer padding for the panel border (px). */
    private static final int OUTER_PADDING = 20;

    /** Insets for {@link GridBagConstraints} (px). */
    private static final int GBC_INSET = 8;

    /** Title font size (pt). */
    private static final float TITLE_FONT_SIZE = 18f;

    /** Grid row indexes. */
    private static final int ROW_TITLE = 0;
    private static final int ROW_USERNAME = 1;
    private static final int ROW_CURRENT_LANG = 2;
    private static final int ROW_PREFERRED_LANG = 3;
    private static final int ROW_SAVE = 4;
    private static final int ROW_ERROR = 5;

    /** Public view name for routing/navigation. */
    private static final String VIEW_NAME = "profile";

    // -------------------- State & collaborators --------------------

    private final ProfileViewModel vm;

    /**
     * Optional save callback. Accepts {@code (username, language)}.
     */
    private BiConsumer<String, Language> onSaveCallback;

    private final JLabel title = new JLabel("Profile", SwingConstants.CENTER);
    private final JTextField usernameFld = new JTextField(15);
    private final JLabel currentLangVal = new JLabel();
    private final JComboBox<Language> langCombo = new JComboBox<Language>();
    private final JButton saveBtn = new JButton("Save");
    private final JLabel errorLabel = new JLabel();

    private final ProfileSetController controller;

    /**
     * Creates the profile view and wires it to the supplied view model and controller.
     *
     * @param viewModel  the view model to observe
     * @param controller the controller used to persist language changes
     */
    public ProfileView(final ProfileViewModel viewModel, final ProfileSetController controller) {
        this.controller = controller;
        this.vm = viewModel;
        this.vm.addPropertyChangeListener(this);

        configureLanguageRenderer();
        buildFormLayout();
        wireSaveButton();

        updateFromState(vm.getState());
    }

    // -------------------- UI assembly --------------------

    /**
     * Configures a renderer that shows {@link Language#displayName()}.
     */
    private void configureLanguageRenderer() {
        langCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    final JList<?> list,
                    final Object value,
                    final int index,
                    final boolean isSelected,
                    final boolean cellHasFocus) {
                final String renderedText;
                if (value instanceof Language) {
                    renderedText = ((Language) value).displayName();
                }
                else {
                    renderedText = String.valueOf(value);
                }
                return super.getListCellRendererComponent(
                        list, renderedText, index, isSelected, cellHasFocus);
            }
        });
    }

    /**
     * Builds the form layout using {@link GridBagLayout}.
     */
    private void buildFormLayout() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(OUTER_PADDING, OUTER_PADDING, OUTER_PADDING, OUTER_PADDING));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(GBC_INSET, GBC_INSET, GBC_INSET, GBC_INSET);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        gbc.gridx = 0;
        gbc.gridy = ROW_TITLE;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        addLabeledComponent("Username:", usernameFld, ROW_USERNAME, gbc);
        usernameFld.setEditable(false);

        addLabeledComponent("Current Language:", currentLangVal, ROW_CURRENT_LANG, gbc);
        addLabeledComponent("Preferred Language:", langCombo, ROW_PREFERRED_LANG, gbc);

        gbc.gridx = 0;
        gbc.gridy = ROW_SAVE;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveBtn, gbc);

        gbc.gridy = ROW_ERROR;
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(errorLabel, gbc);
    }

    /**
     * Adds a two-column labeled component row.
     *
     * @param labelText label text
     * @param comp      component to add at the right column
     * @param row       target grid row
     * @param gbc       constraints instance to reuse
     */
    private void addLabeledComponent(final String labelText, final JComponent comp, final int row,
                                     final GridBagConstraints gbc) {
        gbc.gridy = row;
        gbc.gridx = 0;
        add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        add(comp, gbc);
    }

    /**
     * Wires the save button to the controller and optional callback.
     */
    private void wireSaveButton() {
        saveBtn.addActionListener(event -> {
            if (this.controller != null) {
                this.controller.execute(
                        vm.getState().getUsername(), vm.getState().getOldlanguage(),
                        (Language) langCombo.getSelectedItem());
            }
            if (onSaveCallback != null) {
                onSaveCallback.accept(usernameFld.getText(), (Language) langCombo.getSelectedItem());
            }
        });
    }

    // -------------------- Listeners --------------------

    /**
     * Handles {@link ActionEvent}s from this view.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        // No-op (button wired via lambda). Method kept to satisfy the interface.
    }

    /* ── PropertyChangeListener —— ViewModel -> View ─── */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final ProfileState newState = (ProfileState) evt.getNewValue();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateFromState(newState);
                }
            });
        }
    }

    /**
     * Updates UI controls from the provided state.
     *
     * @param state the profile state
     */
    private void updateFromState(final ProfileState state) {
        usernameFld.setText(state.getUsername());

        if (state.getLanguages() != null) {
            final DefaultComboBoxModel<Language> model = new DefaultComboBoxModel<Language>(state.getLanguages());
            langCombo.setModel(model);
        }

        if (state.getOldlanguage() != null) {
            currentLangVal.setText(state.getOldlanguage().displayName());
            langCombo.setSelectedItem(state.getOldlanguage());
        }
        else {
            currentLangVal.setText("");
        }

        final String err = state.getLanguageError();
        if (err != null) {
            errorLabel.setText(err);
        }
        else {
            errorLabel.setText("");
        }
    }

    /**
     * Registers a callback to be invoked when the user clicks Save.
     *
     * @param callback a consumer receiving {@code (username, language)}
     */
    public void setOnSaveCallback(final BiConsumer<String, Language> callback) {
        this.onSaveCallback = callback;
    }

    /**
     * Returns the logical name of this view.
     *
     * @return the constant view name
     */
    public String getViewName() {
        return VIEW_NAME;
    }
}
