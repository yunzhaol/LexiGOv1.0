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
    private final RankController controller;                     // 可后置注入

    /* ── Swing 组件 ─────────────────────── */
    private final JLabel title = new JLabel("Leaderboard", SwingConstants.CENTER);

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Rank", "Username", "Scores"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(tableModel);

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

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            if (this.controller != null) {
                this.controller.execute(vm.getState().getCurrentUser());   // ★ 统一用 refresh
            }
        });
        add(refresh, BorderLayout.SOUTH);
    }

    /* ── ViewModel → View 同步 ───────────────────────── */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // ① 只关心 state 事件
        if ("state".equals(evt.getPropertyName())) {

            // ② 拿到最新快照
            RankState s = (RankState) evt.getNewValue();

            // ③ 确保在 Swing EDT 中更新 UI，避免线程问题
            SwingUtilities.invokeLater(() -> {

                /* ===== 刷新排行榜表格 ===== */
                tableModel.setRowCount(0);        // 清空旧行
                s.getLeaderboard().forEach(e ->
                        tableModel.addRow(new Object[]{
                                e.getRank(),
                                e.getUsername(),
                                e.getScore()   // ★ 直接使用 DTO 字段
                        })
                );

                /* ===== 更新标题栏 ===== */
                title.setText("Leaderboard — " + s.getCurrentUser());
            });
        }
    }

}
