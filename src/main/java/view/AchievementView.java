package view;

import interface_adapter.achievement.AchievementState;
import interface_adapter.achievement.AchievementViewModel;
import interface_adapter.rank.RankState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Displays the list of achievements newly unlocked by the user.
 * <p>
 * The class listens to {@link AchievementViewModel} for state changes and refreshes the UI accordingly.
 * It also offers a simple callback hook so that the hosting screen can decide what happens when
 * the user clicks the <em>Back</em> button (e.g. switch back to the main menu).
 */
public class AchievementView extends JPanel
        implements PropertyChangeListener, ActionListener {

    private static final long serialVersionUID = 1L;

    /* --------------------------------------------------
     * Constants & collaborators
     * -------------------------------------------------- */
    public static final String VIEW_NAME = "achievement"; // external routers can rely on this name

    private final AchievementViewModel viewModel;

    /* --------------------------------------------------
     * Swing components
     * -------------------------------------------------- */
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String>             achievementList = new JList<>(listModel);
    private final JButton                  backButton      = new JButton("Back");

    /* --------------------------------------------------
     * Optional navigation callback
     * -------------------------------------------------- */
    private Runnable onBackCallback;

    public AchievementView(AchievementViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        initUi();
        // populate initial state in case view model already has data
        updateAchievementList(viewModel.getState().getUnlockedAchievements());
    }

    /* --------------------------------------------------
     * UI assembly
     * -------------------------------------------------- */
    private void initUi() {
        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Achievements Unlocked!", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        add(title, BorderLayout.NORTH);

        // Configure list appearance
        achievementList.setVisibleRowCount(8);
        achievementList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        achievementList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel lbl = (JLabel) new DefaultListCellRenderer()
                    .getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN, 17f));
            lbl.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            return lbl;
        });

        add(new JScrollPane(
                achievementList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(south, BorderLayout.SOUTH);
    }

    /* --------------------------------------------------
     * PropertyChangeListener
     * -------------------------------------------------- */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {

            AchievementState newState = (AchievementState) evt.getNewValue();
            updateAchievementList(newState.getUnlockedAchievements());
        }
    }

    private void updateAchievementList(List<String> unlocked) {
        listModel.clear();
        if (unlocked != null) unlocked.forEach(listModel::addElement);
    }

    /* --------------------------------------------------
     * ActionListener
     * -------------------------------------------------- */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton && onBackCallback != null) {
            onBackCallback.run();
        }
    }

    /* --------------------------------------------------
     * helpers
     * -------------------------------------------------- */
    public void setOnBackCallback(Runnable callback) {
        this.onBackCallback = callback;
    }

    public String getViewName() {
        return VIEW_NAME;
    }
}