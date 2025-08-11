package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import interface_adapter.achievement.AchievementController;
import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.logout.LogoutController;
import interface_adapter.profile.ProfileController;
import interface_adapter.rank.RankController;
import interface_adapter.session.LoggedInState;
import interface_adapter.session.LoggedInViewModel;
import interface_adapter.view_history.ViewHistoryController;

/**
 * Main panel displayed after user login. Provides navigation and loads
 * corresponding subviews.
 */
public class LoggedInView extends JPanel implements PropertyChangeListener {

    /** Logical name of this view for routing/navigation. */
    public static final String VIEW_NAME = "logged in";

    // ---- UI constants (remove magic numbers) ----
    private static final int OUTER_PADDING = 10;
    private static final int USERNAME_BOTTOM_PADDING = 12;
    private static final int NAV_FIXED_WIDTH = 175;
    private static final int NAV_GAP = 24;
    private static final int WELCOME_SIZE = 24;

    // ---- Card names for content panel ----
    private static final String CARD_WELCOME = "Welcome";
    private static final String CARD_PROFILE = "card_profile";
    private static final String CARD_CHECKIN = "card_checkin";
    private static final String CARD_ACHIEVE = "card_achieve";
    private static final String CARD_RANK = "card_rank";
    private static final String CARD_PASSWORD = "card_password";
    private static final String CARD_LOGOUT = "card_logout";
    private static final String CARD_HISTORY = "card_study_history";

    // ---- Controllers (injected) ----
    private AchievementController achievementController;
    private LogoutController logoutController;
    private RankController rankController;
    private ViewHistoryController viewHistoryController;
    private ProfileController profileController;
    private ChangePasswordController changePasswordController;
    private final LoggedInViewModel vm;

    // ---- Layout managers and components ----
    private final CardLayout cards = new CardLayout();
    /** Container for the CardLayout views. */
    private final JPanel contentPanel = new JPanel(cards);
    private final Map<String, JToggleButton> navButtons = new HashMap<>();
    private final ButtonGroup navGroup = new ButtonGroup();

    /* ---------- Username display ---------- */
    /** Shows the current username at the top of the navigation pane. */
    private final JLabel usernameDisplay = new JLabel();

    /**
     * Creates the view, binds to the view model, and initializes the default UI.
     *
     * @param viewModel the view model containing the login state
     */
    public LoggedInView(final LoggedInViewModel viewModel) {
        this.vm = viewModel;
        viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(OUTER_PADDING, OUTER_PADDING, OUTER_PADDING, OUTER_PADDING));

        // Left-side navigation buttons panel
        final JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));

        // Display username
        usernameDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameDisplay.setFont(usernameDisplay.getFont().deriveFont(Font.BOLD));
        usernameDisplay.setBorder(BorderFactory.createEmptyBorder(0, 0, USERNAME_BOTTOM_PADDING, 0));
        nav.add(usernameDisplay);

        addToggle(nav, "Profile", CARD_PROFILE, () -> {
            if (profileController != null) {
                final String user = vm.getState().getUsername();
                profileController.execute(user);
            }
        });
        addToggle(nav, "Daily Check-in", CARD_CHECKIN, null);
        addToggle(nav, "Change Password", CARD_PASSWORD, () -> {
            if (changePasswordController != null) {
                final String user = vm.getState().getUsername();
                changePasswordController.execute(user);
            }
        });
        addToggle(nav, "Achievement", CARD_ACHIEVE, () -> {
            if (achievementController != null) {
                final String user = vm.getState().getUsername();
                achievementController.showAchievements(user);
            }
        });
        addToggle(nav, "View Study History", CARD_HISTORY, () -> {
            if (viewHistoryController != null) {
                final String user = vm.getState().getUsername();
                viewHistoryController.execute(user);
            }
        });
        addToggle(nav, "Rank", CARD_RANK, () -> {
            if (rankController != null) {
                final String user = vm.getState().getUsername();
                rankController.execute(user);
            }
        });
        addToggle(nav, "Log out", CARD_LOGOUT, this::logout);

        // Wrap navigation panel to enforce fixed width
        final JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setPreferredSize(new Dimension(NAV_FIXED_WIDTH, 0));
        wrapper.add(nav, BorderLayout.CENTER);
        add(wrapper, BorderLayout.WEST);

        // Default content panel (welcome view)
        contentPanel.add(new WelcomePanel(), CARD_WELCOME);
        add(contentPanel, BorderLayout.CENTER);

        cards.show(contentPanel, CARD_WELCOME);
    }

    private void logout() {
        if (logoutController != null) {
            final Object[] options = {"Log out", "Cancel"};
            final int choice = JOptionPane.showOptionDialog(
                    null,
                    "Do you confirm log out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]
            );

            if (choice == 0) {
                final String user = vm.getState().getUsername();
                logoutController.execute(user);
            }
        }
    }

    /**
     * Handles property change events from the view model.
     *
     * @param evt the property change event
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final LoggedInState s = (LoggedInState) evt.getNewValue();
            usernameDisplay.setText("User: " + s.getUsername());
        }
    }

    // ------------------ Dependency Injection ------------------

    /**
     * Sets the profile controller.
     *
     * @param controller the controller to invoke for profile actions
     */
    public void setProfileController(final ProfileController controller) {
        this.profileController = controller;
    }

    /**
     * Sets the view-history controller.
     *
     * @param controller the controller to invoke for history actions
     */
    public void setViewHistoryController(final ViewHistoryController controller) {
        this.viewHistoryController = controller;
    }

    /**
     * Sets the logout controller.
     *
     * @param controller the controller to invoke for logout actions
     */
    public void setLogoutController(final LogoutController controller) {
        this.logoutController = controller;
    }

    /**
     * Sets the rank controller.
     *
     * @param controller the controller to invoke for rank actions
     */
    public void setRankController(final RankController controller) {
        this.rankController = controller;
    }

    /**
     * Sets the achievement controller.
     *
     * @param controller the controller to invoke for achievement actions
     */
    public void setAchievementController(final AchievementController controller) {
        this.achievementController = controller;
    }

    /**
     * Sets the change-password controller.
     *
     * @param controller the controller to invoke for password changes
     */
    public void setChangePasswordController(final ChangePasswordController controller) {
        this.changePasswordController = controller;
    }

    // ------------------ Dynamic Subview Injection ------------------

    /**
     * Adds the daily check-in page to the content area.
     *
     * @param checkInView the check-in view component
     */
    public void addStartCheckInPage(final JComponent checkInView) {
        contentPanel.add(checkInView, CARD_CHECKIN);
    }

    /**
     * Adds the achievement page to the content area.
     *
     * @param achievementView the achievement view component
     */
    public void addAchievementPage(final JComponent achievementView) {
        contentPanel.add(achievementView, CARD_ACHIEVE);
    }

    /**
     * Adds the change-password page to the content area.
     *
     * @param changePasswordView the change-password view component
     */
    public void addChangePasswordPage(final ChangePasswordView changePasswordView) {
        contentPanel.add(changePasswordView, CARD_PASSWORD);
    }

    /**
     * Adds the rank page to the content area.
     *
     * @param rankView the rank view component
     */
    public void addRankPage(final RankView rankView) {
        contentPanel.add(rankView, CARD_RANK);
    }

    /**
     * Adds the study-history page to the content area.
     *
     * @param viewHistory the view-history component
     */
    public void addViewHistoryPage(final ViewHistoryView viewHistory) {
        contentPanel.add(viewHistory, CARD_HISTORY);
    }

    /**
     * Adds the profile page to the content area.
     *
     * @param profileView the profile view component
     */
    public void addProfilePage(final ProfileView profileView) {
        contentPanel.add(profileView, CARD_PROFILE);
    }

    /**
     * Returns the name of this view for navigation purposes.
     *
     * @return the constant view name
     */
    public String getViewName() {
        return VIEW_NAME;
    }

    /**
     * Helper to create and register a navigation toggle button.
     *
     * @param parent   the panel to add the button to
     * @param text     the button label
     * @param cardName the card identifier in {@link CardLayout}
     * @param onSelect optional callback when this card is selected
     */
    private void addToggle(final JPanel parent, final String text, final String cardName, final Runnable onSelect) {
        final JToggleButton btn = new JToggleButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.addActionListener(event -> {
            cards.show(contentPanel, cardName);
            btn.setSelected(true);
            if (onSelect != null) {
                onSelect.run();
            }
        });
        navGroup.add(btn);
        navButtons.put(cardName, btn);
        parent.add(btn);
        parent.add(Box.createVerticalStrut(NAV_GAP));
    }

    /** Placeholder panel shown by default; can be replaced by a real ProfileView. */
    private static class WelcomePanel extends JPanel {

        /** Builds the simple welcome panel. */
        WelcomePanel() {
            setLayout(new BorderLayout());
            final JLabel lbl = new JLabel("Welcome!!!", SwingConstants.CENTER);
            lbl.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, WELCOME_SIZE));
            add(lbl, BorderLayout.CENTER);
        }
    }
}
