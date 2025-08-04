package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import interface_adapter.view_history.ViewHistoryState;
import interface_adapter.view_history.ViewHistoryViewModel;
import use_case.viewhistory.ViewHistoryEntryData;

/**
 * Displays the user's study-history sessions in a table along with a summary.
 *
 * <p>
 * This view observes {@link ViewHistoryViewModel} and updates the table and
 * summary label when the state changes.
 * </p>
 */
public class ViewHistoryView extends JPanel implements PropertyChangeListener {

    // ---- UI constants (remove magic numbers) ----
    private static final int BORDER_HGAP = 10;
    private static final int BORDER_VGAP = 10;
    private static final int PADDING = 16;
    private static final float TITLE_FONT_SIZE = 18f;

    // ---- collaborators ----
    private final ViewHistoryViewModel viewModel;

    // ---- components ----
    private final JLabel title = new JLabel(ViewHistoryViewModel.TITLE_LABEL, SwingConstants.CENTER);

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[] {"#", "Date", "Words Learned" }, 0) {

        @Override
        public boolean isCellEditable(final int row, final int column) {
            return false;
        }
    };

    private final JTable table = new JTable(tableModel);
    private final JLabel summaryLabel = new JLabel();

    /**
     * Creates the view and wires it to the provided view model.
     *
     * @param viewModel the view model to observe
     */
    public ViewHistoryView(final ViewHistoryViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(BORDER_HGAP, BORDER_VGAP));
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        add(title, BorderLayout.NORTH);

        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        final JPanel south = new JPanel(new BorderLayout());
        south.add(summaryLabel, BorderLayout.WEST);
        add(south, BorderLayout.SOUTH);

        updateFromState(this.viewModel.getState());
    }

    /**
     * Receives state changes and refreshes the UI.
     *
     * @param evt the property change event (only the {@code "state"} key is handled)
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final ViewHistoryState state = (ViewHistoryState) evt.getNewValue();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateFromState(state);
                }
            });
        }
    }

    /**
     * Applies the given state to the title, table rows, and summary label.
     *
     * @param state the state to render
     */
    private void updateFromState(final ViewHistoryState state) {

        // ASCII-only separator to satisfy style rule.
        title.setText(ViewHistoryViewModel.TITLE_LABEL + " - " + state.getCurrentUser());

        tableModel.setRowCount(0);
        final List<ViewHistoryEntryData> sessions = state.getSessions();

        for (int i = 0; i < sessions.size(); i++) {
            final ViewHistoryEntryData entry = sessions.get(i);
            final String dateStr;
            final int words;
            if (entry != null && entry.getEndTime() != null) {
                dateStr = entry.getEndTime();
                words = entry.getWordsCount();
            }
            else {
                dateStr = entry.toString();
                words = 0;
            }
            tableModel.addRow(new Object[] {i + 1, dateStr, words });
        }

        summaryLabel.setText(String.format("%s %d    %s %d",
                ViewHistoryViewModel.SESSIONS_LABEL, state.getTotalSessions(),
                ViewHistoryViewModel.WORDS_LABEL, state.getTotalWords()));

        if (state.getErrorMessage() != null) {
            JOptionPane.showMessageDialog(
                    this, state.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
