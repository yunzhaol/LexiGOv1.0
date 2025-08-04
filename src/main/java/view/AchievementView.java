package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import interface_adapter.achievement.AchievementState;
import interface_adapter.achievement.AchievementViewModel;

/**
 * Displays the list of achievements newly unlocked by the user.
 *
 * <p>
 * The class listens to {@link AchievementViewModel} for state changes and refreshes
 * the UI accordingly. It also exposes a simple callback so the hosting screen
 * can decide what happens when the user clicks the Back button.
 */
public class AchievementView extends JPanel implements PropertyChangeListener, ActionListener {

    /** Logical name of this view for routing/navigation. */
    public static final String VIEW_NAME = "achievement";

    private static final long serialVersionUID = 1L;

    // -------------------- UI constants (magic numbers eliminated) --------------------

    /** Vertical gap used by the main BorderLayout. */
    private static final int LAYOUT_VGAP = 8;

    /** Outer padding for the container border. */
    private static final int OUTER_PADDING = 16;

    /** Number of visible rows in the achievements list. */
    private static final int VISIBLE_ROW_COUNT = 8;

    /** Font size for the title label. */
    private static final float TITLE_FONT_SIZE = 18f;

    /** Font size for each list item. */
    private static final float ITEM_FONT_SIZE = 17f;

    /** Vertical padding for a list cell (top/bottom). */
    private static final int CELL_PADDING_V = 4;

    /** Horizontal padding for a list cell (left/right). */
    private static final int CELL_PADDING_H = 8;

    // -------------------- Collaborators --------------------

    private final AchievementViewModel viewModel;

    // -------------------- Swing components --------------------

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> achievementList = new JList<>(listModel);
    private final JButton backButton = new JButton("Back");

    // -------------------- Optional navigation callback --------------------

    private Runnable onBackCallback;

    /**
     * Creates the view and registers it as a listener of the given view model.
     *
     * @param viewModel the view model to observe; must not be {@code null}
     */
    public AchievementView(final AchievementViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        initUi();
        // populate initial state in case view model already has data
        updateAchievementList(viewModel.getState().getUnlockedAchievements());
    }

    // -------------------- UI assembly --------------------

    /** Builds and lays out all Swing components. */
    private void initUi() {
        setLayout(new BorderLayout(0, LAYOUT_VGAP));
        setBorder(BorderFactory.createEmptyBorder(OUTER_PADDING, OUTER_PADDING, OUTER_PADDING, OUTER_PADDING));

        final JLabel title = new JLabel("Achievements Unlocked!", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        add(title, BorderLayout.NORTH);

        // Configure list appearance
        achievementList.setVisibleRowCount(VISIBLE_ROW_COUNT);
        achievementList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        achievementList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            final JLabel lbl = (JLabel) new DefaultListCellRenderer()
                    .getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN, ITEM_FONT_SIZE));
            lbl.setBorder(BorderFactory.createEmptyBorder(CELL_PADDING_V,
                    CELL_PADDING_H, CELL_PADDING_V, CELL_PADDING_H));
            return lbl;
        });

        add(new JScrollPane(
                achievementList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

        final JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(south, BorderLayout.SOUTH);
    }

    // -------------------- PropertyChangeListener --------------------

    /**
     * Reacts to state updates from the view model and refreshes the UI.
     *
     * @param evt the property change event; only the "state" property is consumed
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final AchievementState newState = (AchievementState) evt.getNewValue();
            updateAchievementList(newState.getUnlockedAchievements());
        }
    }

    /**
     * Rebuilds the list model with the supplied unlocked achievements.
     *
     * @param unlocked the unlocked achievement names; may be {@code null}
     */
    private void updateAchievementList(final List<String> unlocked) {
        listModel.clear();
        if (unlocked != null) {
            unlocked.forEach(listModel::addElement);
        }
    }

    // -------------------- ActionListener --------------------

    /**
     * Handles UI actions fired by components of this view.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == backButton && onBackCallback != null) {
            onBackCallback.run();
        }
    }

    // -------------------- Helpers --------------------

    /**
     * Registers a callback invoked when the user navigates back.
     *
     * @param callback the callback to run; may be {@code null} to clear it
     */
    public void setOnBackCallback(final Runnable callback) {
        this.onBackCallback = callback;
    }

    /**
     * Returns the logical view name to be used by external routers.
     *
     * @return the view name constant
     */
    public String getViewName() {
        return VIEW_NAME;
    }
}
