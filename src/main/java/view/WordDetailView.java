package view;


import interface_adapter.studysession.word_detail.WordDetailController;
import interface_adapter.studysession.word_detail.WordDetailState;
import interface_adapter.studysession.word_detail.WordDetailViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class WordDetailView extends JPanel implements PropertyChangeListener {


    private static final String VIEW_NAME = "word detail";

    private final WordDetailViewModel viewModel;
    private WordDetailController     controller;

    private final JLabel transLabel   = new JLabel("Translation", SwingConstants.CENTER);
    private final JLabel exampleLabel = new JLabel("Example",     SwingConstants.CENTER);
    private final JButton flipBack    = new JButton("Flip back");


    public WordDetailView(WordDetailViewModel vm) {
        this.viewModel = vm;
        vm.addPropertyChangeListener(this);

        transLabel.setFont(transLabel.getFont().deriveFont(Font.PLAIN, 18f));
        exampleLabel.setFont(exampleLabel.getFont().deriveFont(Font.ITALIC, 16f));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        transLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        exampleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(45));
        add(transLabel);
        add(Box.createVerticalStrut(25));
        add(exampleLabel);
        add(Box.createVerticalStrut(20));


        JPanel nav = new JPanel();
        nav.add(flipBack);
        add(nav);

        flipBack.addActionListener(e -> {
            if (controller != null) controller.switchToStudySessionView();
        });
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            WordDetailState s = (WordDetailState) evt.getNewValue();
            transLabel.setText(s.getTranslation());
            exampleLabel.setText("<html><body style='text-align:center'>" +
                    s.getExample() + "</body></html>");
        }
    }


    public String getViewName() { return VIEW_NAME; }

    public void setWordDetailController(WordDetailController c) {
        this.controller = c;
    }
}

