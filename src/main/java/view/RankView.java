package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import interface_adapter.rank.RankController;
import interface_adapter.rank.RankState;
import interface_adapter.rank.RankViewModel;

/**
 * Displays a leaderboard table and the current user's position.
 *
 * <p>
 * This view listens to {@link RankViewModel} state changes and refreshes the UI
 * accordingly. A {@link RankController} can be injected to trigger refreshes.
 * </p>
 */
public class RankView extends JPanel implements PropertyChangeListener {

    // ---- UI constants (remove magic numbers) ----
    private static final int BORDER_HGAP = 10;
    private static final int BORDER_VGAP = 10;
    private static final float TITLE_FONT_SIZE = 18f;

    // ---- collaborators ----
    private final RankViewModel viewModel;
    private RankController controller;

    // ---- components ----
    private final JLabel title = new JLabel("Leaderboard", SwingConstants.CENTER);

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[] {"Rank", "Username", "Scores" }, 0) {

        @Override
        public boolean isCellEditable(final int row, final int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);
    private final JLabel currentPosLabel = new JLabel("", SwingConstants.CENTER);

    /**
     * Creates the view and wires it to the given view model and controller.
     *
     * @param viewModel  the view model to observe
     * @param controller the controller used to fetch/refresh rankings
     */
    public RankView(final RankViewModel viewModel, final RankController controller) {
        this.viewModel = viewModel;
        this.controller = controller;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(BORDER_HGAP, BORDER_VGAP));

        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        add(title, BorderLayout.NORTH);

        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        currentPosLabel.setFont(currentPosLabel.getFont().deriveFont(Font.BOLD));
        add(currentPosLabel, BorderLayout.SOUTH);

        final JButton refresh = new JButton("Refresh");
        refresh.addActionListener(event -> {
            if (this.controller != null) {
                this.controller.execute(viewModel.getState().getCurrentUser());
            }
        });
        add(refresh, BorderLayout.EAST);
    }

    /**
     * Receives updates from the view model and applies them to the UI.
     *
     * @param evt the property change event; only the {@code "state"} key is handled
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final RankState state = (RankState) evt.getNewValue();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    applyState(state);
                }
            });
        }
    }

    /**
     * Applies the given state to the table, title, and selection.
     *
     * @param state the new state from the view model
     */
    private void applyState(final RankState state) {
        tableModel.setRowCount(0);
        state.getLeaderboard().forEach(entry -> {
            tableModel.addRow(new Object[] {
                    entry.getRank(),
                    entry.getUsername(),
                    entry.getScore(),
            });
        });

        // ASCII-only text to satisfy style rule that forbids non-ASCII characters.
        title.setText("Leaderboard - " + state.getCurrentUser());
        currentPosLabel.setText("Your current position: " + state.getPosition());

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 1).equals(state.getCurrentUser())) {
                table.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    /**
     * Injects the controller used to refresh rankings.
     *
     * @param controller the controller instance
     */
    public void setController(final RankController controller) {
        this.controller = controller;
    }
}
