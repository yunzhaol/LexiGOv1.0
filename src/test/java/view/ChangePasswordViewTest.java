package view;

import interface_adapter.change_password.ChangeViewModel;
import interface_adapter.change_password.MakePasswordChange.MakePasswordChangeController;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ChangePasswordViewTest {

    @Test
    public void constructSetControllerAndPropertyChange() {
        ChangeViewModel vm = new ChangeViewModel();
        ChangePasswordView view = new ChangePasswordView(vm);

        // Set a mocked controller (keeps test light and fast)
        MakePasswordChangeController controller = mock(MakePasswordChangeController.class);
        view.setController(controller);

        // Name check (expected: "change password")
        assertNotNull(view.getViewName());
        assertFalse(view.getViewName().trim().isEmpty());

        // Trigger property change
        PropertyChangeEvent evt = new PropertyChangeEvent(vm, "state", null, vm.getState());
        assertDoesNotThrow(() -> view.propertyChange(evt));
    }
}