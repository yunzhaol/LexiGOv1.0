package view;

import interface_adapter.ViewManagerModel;
import org.junit.jupiter.api.Test;

import javax.swing.JPanel;
import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;

import static org.junit.jupiter.api.Assertions.*;

public class ViewManagerTest {

    @Test
    public void propertyChangeShowsCard() {
        // Prepare a simple card container
        CardLayout layout = new CardLayout();
        JPanel views = new JPanel(layout);
        JPanel cardA = new JPanel();
        JPanel cardB = new JPanel();
        views.add(cardA, "A");
        views.add(cardB, "B");

        ViewManagerModel model = new ViewManagerModel();
        ViewManager manager = new ViewManager(views, layout, model);

        // Fire a property change to switch to "B"
        PropertyChangeEvent evt = new PropertyChangeEvent(model, "state", "A", "B");
        assertDoesNotThrow(() -> manager.propertyChange(evt));
    }
}