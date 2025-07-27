package view;

import interface_adapter.start_checkin.StartCheckInController;
import interface_adapter.start_checkin.StartCheckInState;
import interface_adapter.start_checkin.StartCheckInViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Screen that lets the user specify how many new words to study today.
 * No cancel button: once here, user can only start the session.
 */
public class StartCheckInView extends JPanel implements PropertyChangeListener {

    private final String viewName = "start checkin";
    private final StartCheckInViewModel viewModel;

    private final JTextField numberField = new JTextField(4);
    private final JLabel    numberError = new JLabel();

    private final JButton   startButton = new JButton("Start ➜");

    private StartCheckInController controller;

    public StartCheckInView(StartCheckInViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        /* -------------------------  build UI  ------------------------- */
        JLabel title = new JLabel("Daily Check‑In");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        LabelTextPanel numberInfo = new LabelTextPanel(new JLabel("Words to learn"), numberField);

        JPanel buttons = new JPanel();
        buttons.add(startButton);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(title);
        add(numberInfo);
        add(numberError);
        add(buttons);

        /*  ---------------------  event wiring  ----------------------- */
        numberField.getDocument().addDocumentListener(new DocumentListener() {
            private void sync() {
                StartCheckInState s = viewModel.getState();
                s.setNumberWords(numberField.getText());
                viewModel.setState(s);
            }
            public void insertUpdate(DocumentEvent e){sync();}
            public void removeUpdate(DocumentEvent e){sync();}
            public void changedUpdate(DocumentEvent e){sync();}
        });

        startButton.addActionListener(e -> {
            StartCheckInState s = viewModel.getState();
            controller.execute(s.getUsername(), s.getNumberWords());
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            StartCheckInState state = (StartCheckInState) evt.getNewValue();
            numberError.setText(state.getInputNumberError());
            numberField.setText(state.getNumberWords());
        }
    }

    public String getViewName() { return viewName; }
    public void setController(StartCheckInController c){ this.controller = c; }
}
