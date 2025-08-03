package view;


import interface_adapter.studysession.word_detail.WordDetailController;
import interface_adapter.studysession.word_detail.WordDetailState;
import interface_adapter.studysession.word_detail.WordDetailViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * 仅显示翻译和例句的单词详情视图（不再展示原单词本身）。
 */
public class WordDetailView extends JPanel implements PropertyChangeListener {

    /* --- 常量 --- */
    private static final String VIEW_NAME = "word detail";

    /* --- MVC 成员 --- */
    private final WordDetailViewModel viewModel;
    private WordDetailController     controller;

    /* --- UI 组件 --- */
    private final JLabel transLabel   = new JLabel("Translation", SwingConstants.CENTER);
    private final JLabel exampleLabel = new JLabel("Example",     SwingConstants.CENTER);
    private final JButton flipBack    = new JButton("Flip back");

    /* ------------------------------------------------------------ */

    public WordDetailView(WordDetailViewModel vm) {
        this.viewModel = vm;
        vm.addPropertyChangeListener(this);

        /* ---------- 字体 ---------- */
        transLabel.setFont(transLabel.getFont().deriveFont(Font.PLAIN, 18f));
        exampleLabel.setFont(exampleLabel.getFont().deriveFont(Font.ITALIC, 16f));

        /* ---------- 布局 ---------- */
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        transLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        exampleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(45));  // 上边距
        add(transLabel);
        add(Box.createVerticalStrut(25));
        add(exampleLabel);
        add(Box.createVerticalStrut(20));



        // 按钮区域
        JPanel nav = new JPanel();
        nav.add(flipBack);
        add(nav);

        /* ---------- 事件 ---------- */
        flipBack.addActionListener(e -> {
            if (controller != null) controller.switchToStudySessionView();
        });
    }

    /* ================= PropertyChangeListener ================= */

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            WordDetailState s = (WordDetailState) evt.getNewValue();
            transLabel.setText(s.getTranslation());
            exampleLabel.setText("<html><body style='text-align:center'>" +
                    s.getExample() + "</body></html>");
        }
    }

    /* ================= 公共方法 ================= */

    public String getViewName() { return VIEW_NAME; }

    public void setWordDetailController(WordDetailController c) {
        this.controller = c;
    }
}

