package view;

import interface_adapter.view_history.ViewHistoryViewModel;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;

import static org.junit.jupiter.api.Assertions.*;

public class ViewHistoryViewUITest {

    @Test
    public void constructAndPropertyChange() {
        ViewHistoryViewModel vm = new ViewHistoryViewModel();
        ViewHistoryView view = new ViewHistoryView(vm);

        // View may not expose a name; at least ensure instantiation works
        PropertyChangeEvent evt = new PropertyChangeEvent(vm, "state", null, vm.getState());
        assertDoesNotThrow(() -> view.propertyChange(evt));
    }
}