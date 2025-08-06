package view;

import interface_adapter.rank.RankViewModel;
import interface_adapter.rank.RankController;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class RankViewTest {

    @Test
    public void constructAndPropertyChange() {
        RankViewModel vm = new RankViewModel();
        RankController controller = mock(RankController.class);
        RankView view = new RankView(vm, controller);

        // Property change should be safe
        PropertyChangeEvent evt = new PropertyChangeEvent(vm, "state", null, vm.getState());
        assertDoesNotThrow(() -> view.propertyChange(evt));
    }
}