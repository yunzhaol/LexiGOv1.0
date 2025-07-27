package view;

import interface_adapter.achievement.AchievementController;
import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.profile.ProfileController;
import interface_adapter.session.LoggedInState;
import interface_adapter.session.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.rank.RankController;
import interface_adapter.view_history.ViewHistoryController;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;


/**
 * Main panel displayed after user login. Provides navigation and loads corresponding subviews.
 */
public class LoggedInView extends JPanel implements PropertyChangeListener {

    // Constants for view identification
    public static final String VIEW_NAME = "logged in";
    private static final String CARD_PROFILE       = "card_profile";
    private static final String CARD_CHECKIN       = "card_checkin";
    private static final String CARD_ACHIEVE       = "card_achieve";
    private static final String CARD_RANK          = "card_rank";
    private static final String CARD_PASSWORD      = "card_password";
    private static final String CARD_LOGOUT        = "card_logout";
    private static final String CARD_HISTORY       = "card_study_history";

    // Dependency-injected controllers for handling user actions
    private AchievementController achievementController;
    private LogoutController logoutController;
    private RankController rankController;
    private ViewHistoryController viewHistoryController;
    private ProfileController profileController;
    private ChangePasswordController changePasswordController;
    private final LoggedInViewModel vm;

    // Layout managers and components
    private final CardLayout cards = new CardLayout();
    private final JPanel contentPanel = new JPanel(cards);   // Container for card layout views
    private final Map<String, JToggleButton> navButtons = new HashMap<>();
    private final ButtonGroup navGroup = new ButtonGroup();

    /**
     * Constructor: sets up UI, binds view model, and initializes default view.
     * @param vm the view model containing login state and observers
     */
    public LoggedInView(LoggedInViewModel vm) {
        this.vm = vm;
        vm.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left-side navigation buttons panel
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));

        addToggle(nav, "Profile", CARD_PROFILE, () -> {
            if (profileController != null) {
                String user = vm.getState().getUsername();
                profileController.execute(user);
            }
        });
        addToggle(nav, "Daily Check-in", CARD_CHECKIN, null);
        addToggle(nav, "Change Password", CARD_PASSWORD, () -> {
            if (changePasswordController != null) {
                String user = vm.getState().getUsername();
                changePasswordController.execute(user);
            }
        });
        addToggle(nav, "Achievement", CARD_ACHIEVE, () -> {
            if (achievementController != null) {
                String user = vm.getState().getUsername();
                achievementController.showAchievements(user);
            }
        });
        addToggle(nav, "View Study History", CARD_HISTORY, () -> {
            if (viewHistoryController != null) {
                String user = vm.getState().getUsername();
                viewHistoryController.execute(user);
            }
        });
        addToggle(nav, "Rank", CARD_RANK, () -> {
            if (rankController != null) {
                String user = vm.getState().getUsername();
                rankController.execute(user);
            }
        });
        addToggle(nav, "Log out", CARD_LOGOUT, () -> {
            if (logoutController != null) {
                String user = vm.getState().getUsername();
                logoutController.execute(user);
            }
        });

        // Wrap navigation panel to enforce fixed width
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setPreferredSize(new Dimension(160, 0));
        wrapper.add(nav, BorderLayout.CENTER);
        add(wrapper, BorderLayout.WEST);

        // Default content panel (profile view)
        contentPanel.add(new ProfilePanel(), CARD_PROFILE);
        add(contentPanel, BorderLayout.CENTER);

        // Select and show profile view by default
        navButtons.get(CARD_PROFILE).setSelected(true);
        cards.show(contentPanel, CARD_PROFILE);
    }

    /**
     * Helper to create and register a navigation toggle button.
     * @param parent   the panel to add the button to
     * @param text     the button label
     * @param cardName the card identifier in CardLayout
     * @param onSelect optional callback when this card is selected
     */
    private void addToggle(JPanel parent, String text, String cardName, Runnable onSelect) {
        JToggleButton btn = new JToggleButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.addActionListener(e -> {
            cards.show(contentPanel, cardName);
            btn.setSelected(true);
            if (onSelect != null) onSelect.run();
        });
        navGroup.add(btn);
        navButtons.put(cardName, btn);
        parent.add(btn);
        parent.add(Box.createVerticalStrut(8));
    }

    /**
     * Handles property change events from the view model.
     * @param evt the property change event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            LoggedInState s = (LoggedInState) evt.getNewValue();
            // TODO: propagate new state (e.g., username) to subviews as needed
        }
    }

    // ------------------ Dependency Injection ------------------

    public void setProfileController(ProfileController profileController) {
        this.profileController = profileController;
    }

    public void setViewHistoryController(ViewHistoryController viewHistoryController) {
        this.viewHistoryController = viewHistoryController;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

    public void setRankController(RankController rankController) {
        this.rankController = rankController;
    }

    public void setAchievementController(AchievementController achievementController) {
        this.achievementController = achievementController;
    }

    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }

    // ------------------ Dynamic Subview Injection ------------------

    public void addStartCheckInPage(JComponent checkInView) {
        contentPanel.add(checkInView, CARD_CHECKIN);
    }

    public void addAchievementPage(JComponent achievementView) {
        contentPanel.add(achievementView, CARD_ACHIEVE);
    }

    public void addChangePasswordPage(ChangePasswordView changePasswordView) {
        contentPanel.add(changePasswordView, CARD_PASSWORD);
    }

    public void addRankPage(RankView rankView) {
        contentPanel.add(rankView, CARD_RANK);
    }

    public void addViewHistoryPage(ViewHistoryView viewHistory) {
        contentPanel.add(viewHistory, CARD_HISTORY);
    }

    public void addProfilePage(ProfileView profileView) {
        contentPanel.add(profileView, CARD_PROFILE);
    }

    /**
     * Returns the name of this view for navigation purposes.
     * @return view name
     */
    public String getViewName() { return VIEW_NAME; }


    /**
     * Placeholder profile panel shown by default; can be replaced by real ProfileView.
     */
    private static class ProfilePanel extends JPanel {
        ProfilePanel() { add(new JLabel("Profile Page")); }
    }
}
