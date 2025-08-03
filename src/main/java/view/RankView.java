package view;

import interface_adapter.rank.RankController;
import interface_adapter.rank.RankState;
import interface_adapter.rank.RankViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class RankView extends JPanel implements PropertyChangeListener {


    private final RankViewModel vm;
    private RankController controller;


    private final JLabel title = new JLabel("Leaderboard", SwingConstants.CENTER);

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Rank", "Username", "Scores"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(tableModel);


    private final JLabel currentPosLabel = new JLabel("", SwingConstants.CENTER);


    public RankView(RankViewModel vm, RankController controller) {
        this.vm = vm;
        this.controller = controller;
        this.vm.addPropertyChangeListener(this);
        setLayout(new BorderLayout(10, 10));

        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        add(title, BorderLayout.NORTH);

        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        currentPosLabel.setFont(currentPosLabel.getFont().deriveFont(Font.BOLD));
        add(currentPosLabel, BorderLayout.SOUTH);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            if (this.controller != null) {
                this.controller.execute(vm.getState().getCurrentUser());
            }
        });
        add(refresh, BorderLayout.EAST);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if ("state".equals(evt.getPropertyName())) {

            RankState s = (RankState) evt.getNewValue();
            SwingUtilities.invokeLater(() -> {

                tableModel.setRowCount(0);
                s.getLeaderboard().forEach(e ->
                        tableModel.addRow(new Object[]{
                                e.getRank(),
                                e.getUsername(),
                                e.getScore()
                        })
                );

                title.setText("Leaderboard — " + s.getCurrentUser());
                currentPosLabel.setText("Your current position: " + s.getPosition());

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (tableModel.getValueAt(i, 1).equals(s.getCurrentUser())) {
                        table.setRowSelectionInterval(i, i);
                        break;
                    }
                }
            });
        }
    }

    public void setController(RankController controller) {
        this.controller = controller;
    }
}
