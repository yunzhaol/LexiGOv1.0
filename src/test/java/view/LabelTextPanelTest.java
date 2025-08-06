package view;

import org.junit.jupiter.api.Test;

import javax.swing.JLabel;
import javax.swing.JTextField;

import static org.junit.jupiter.api.Assertions.*;

public class LabelTextPanelTest {

    @Test
    public void addTwoComponents() {
        JLabel label = new JLabel("Name");
        JTextField field = new JTextField(10);

        LabelTextPanel panel = new LabelTextPanel(label, field);
        assertEquals(2, panel.getComponentCount());
        assertSame(label, panel.getComponent(0));
        assertSame(field, panel.getComponent(1));
    }
}