package view;

import interface_adapter.signup.SignupViewModel;
import interface_adapter.signup.SignupController;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class SignupViewTest {

    @Test
    public void constructSetControllerAndPropertyChange() {
        SignupViewModel vm = new SignupViewModel();
        SignupView view = new SignupView(vm);

        // Set controller via setter to exercise wiring

        // View name should be a non-empty string
        assertNotNull(view.getViewName());
        assertFalse(view.getViewName().trim().isEmpty());

        // Drive property change to update fields and avoid UI exceptions
        PropertyChangeEvent evt = new PropertyChangeEvent(vm, "state", null, vm.getState());
        assertDoesNotThrow(() -> view.propertyChange(evt));
    }
}