package view;

import interface_adapter.studysession.StudySessionController;
import interface_adapter.studysession.StudySessionState;
import interface_adapter.studysession.StudySessionViewModel;
import interface_adapter.finish.FinishController;
import interface_adapter.studysession.word_detail.WordDetailController;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Shows the current card (word) and lets the learner navigate through
 * the deck produced by StartCheckIn.
 */
public class StudySessionView extends JPanel implements PropertyChangeListener {

    private final String viewName = "study session";
    private final StudySessionViewModel viewModel;

    private final JLabel wordLabel = new JLabel("Word", SwingConstants.CENTER);
    private final JLabel progressLabel = new JLabel("0 / 0", SwingConstants.CENTER);

    private final JButton prev = new JButton("◀ Prev");
    private final JButton next = new JButton("Next ▶");
    private final JButton flip = new JButton("Flip");
    private final JButton finish = new JButton("Finish ✓");

    private WordDetailController wordDetailController;
    private StudySessionController navController;
    private FinishController finishController;

    public StudySessionView(StudySessionViewModel vm){
        this.viewModel = vm;
        vm.addPropertyChangeListener(this);

        wordLabel.setFont(wordLabel.getFont().deriveFont(24f));

        JPanel nav = new JPanel();
        nav.add(prev);
        nav.add(next);
        nav.add(flip);
        nav.add(finish);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalStrut(20));
        add(wordLabel);
        add(progressLabel);
        add(Box.createVerticalStrut(10));
        add(nav);

        prev.addActionListener(e -> navController.handlePrevRequest(vm.getState().getPagenumber(),
                vm.getState().getTotalpage()));
        next.addActionListener(e -> navController.handleNextRequest(vm.getState().getPagenumber(),
                vm.getState().getTotalpage()));
        flip.addActionListener(e -> wordDetailController.execute(vm.getState().getPagenumber()));
        finish.addActionListener(e -> finishController.execute(vm.getState().getUsername()));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())){
            StudySessionState s = (StudySessionState) evt.getNewValue();
            wordLabel.setText(s.getWord());
            progressLabel.setText((s.getPagenumber())+" / "+s.getTotalpage());
            prev.setEnabled(!s.isReachfirst());
            flip.setEnabled(!s.getWord().equals("Welcome"));
            next.setEnabled(!s.isReachlast());
            finish.setEnabled(s.isReachlast());

        }
    }

    public String getViewName(){ return viewName; }
    public void setStudySessionController(StudySessionController c){ this.navController = c; }
    public void setFinishCheckInController(FinishController c){ this.finishController = c; }
    public void setWordDetailController(WordDetailController c){ this.wordDetailController = c; }
}