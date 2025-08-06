package view;

import interface_adapter.login.LoginViewModel;
import interface_adapter.login.LoginController;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class LoginViewTest {

    @Test
    public void constructSetControllerAndPropertyChange() {
        LoginViewModel vm = new LoginViewModel();
        LoginView view = new LoginView(vm);

        // Set controller via the provided setter
        view.setLoginController(mock(LoginController.class));

        // View name should be a non-empty string
        assertNotNull(view.getViewName());
        assertFalse(view.getViewName().trim().isEmpty());

        // Trigger property change
        PropertyChangeEvent evt = new PropertyChangeEvent(vm, "state", null, vm.getState());
        assertDoesNotThrow(() -> view.propertyChange(evt));
    }
}