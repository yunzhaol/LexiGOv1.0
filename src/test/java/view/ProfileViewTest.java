package view;

import interface_adapter.profile.ProfileViewModel;
import interface_adapter.profile.profile_set.ProfileSetController;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ProfileViewTest {

    @Test
    public void constructAndPropertyChange() {
        ProfileViewModel vm = new ProfileViewModel();
        ProfileSetController controller = mock(ProfileSetController.class);
        ProfileView view = new ProfileView(vm, controller);

        assertEquals("profile", view.getViewName());

        PropertyChangeEvent evt = new PropertyChangeEvent(vm, "state", null, vm.getState());
        assertDoesNotThrow(() -> view.propertyChange(evt));
    }
}