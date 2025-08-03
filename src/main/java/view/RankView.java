package view;

import interface_adapter.rank.RankController;
import interface_adapter.rank.RankState;
import interface_adapter.rank.RankViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/** 排行榜页面：监听 RankViewModel 的 "leaderboard" / "currentUser" 属性来刷新。 */
public class RankView extends JPanel implements PropertyChangeListener {

    /* ── 依赖 ───────────────────────────── */
    private final RankViewModel vm;
    private RankController controller;                     // 可后置注入

    /* ── Swing 组件 ─────────────────────── */
    private final JLabel title = new JLabel("Leaderboard", SwingConstants.CENTER);

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Rank", "Username", "Scores"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    /** 显示当前登录用户的名次 */
    private final JLabel currentPosLabel = new JLabel("", SwingConstants.CENTER);

    /* ── 构造 ───────────────────────────── */
    public RankView(RankViewModel vm, RankController controller) {
        this.vm = vm;
        this.controller = controller;          // 允许 null，稍后再注入
        this.vm.addPropertyChangeListener(this);
        setLayout(new BorderLayout(10, 10));

        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        add(title, BorderLayout.NORTH);

        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        /* 当前排名标签样式 */
        currentPosLabel.setFont(currentPosLabel.getFont().deriveFont(Font.BOLD));
        add(currentPosLabel, BorderLayout.SOUTH);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            if (this.controller != null) {
                this.controller.execute(vm.getState().getCurrentUser());   // ★ 统一用 refresh
            }
        });
        add(refresh, BorderLayout.EAST);
    }

    /* ── ViewModel → View 同步 ───────────────────────── */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // ① 只关心 state 事件
        if ("state".equals(evt.getPropertyName())) {
            // ② 拿到最新快照
            RankState s = (RankState) evt.getNewValue();
            SwingUtilities.invokeLater(() -> {
                /* ===== 刷新排行榜表格 ===== */
                tableModel.setRowCount(0);        // 清空旧行
                s.getLeaderboard().forEach(e ->
                        tableModel.addRow(new Object[]{
                                e.getRank(),
                                e.getUsername(),
                                e.getScore()
                        })
                );

                /* ===== 更新标题 & 当前名次标签 ===== */
                title.setText("Leaderboard — " + s.getCurrentUser());
                currentPosLabel.setText("Your current position: " + s.getPosition());

                // 高亮当前用户所在行
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (tableModel.getValueAt(i, 1).equals(s.getCurrentUser())) {
                        table.setRowSelectionInterval(i, i);
                        break;
                    }
                }
            });
        }
    }

    /* 允许运行期注入 / 替换 Controller */
    public void setController(RankController controller) {
        this.controller = controller;
    }
}
