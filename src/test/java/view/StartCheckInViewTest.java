package view;

import interface_adapter.start_checkin.StartCheckInViewModel;
import interface_adapter.start_checkin.StartCheckInController;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class StartCheckInViewTest {

    @Test
    public void constructSetControllerAndPropertyChange() {
        StartCheckInViewModel vm = new StartCheckInViewModel();
        StartCheckInView view = new StartCheckInView(vm);

        // Use the actual setter name defined in StartCheckInView
        view.setController(mock(StartCheckInController.class));

        assertNotNull(view.getViewName());
        assertFalse(view.getViewName().trim().isEmpty());

        PropertyChangeEvent evt = new PropertyChangeEvent(vm, "state", null, vm.getState());
        assertDoesNotThrow(() -> view.propertyChange(evt));
    }
}