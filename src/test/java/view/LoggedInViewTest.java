package view;

import interface_adapter.session.LoggedInViewModel;
import interface_adapter.achievement.AchievementController;
import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.logout.LogoutController;
import interface_adapter.profile.ProfileController;
import interface_adapter.rank.RankController;
import interface_adapter.view_history.ViewHistoryController;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class LoggedInViewTest {

    @Test
    public void constructSetControllersAndPropertyChange() {
        LoggedInViewModel vm = new LoggedInViewModel();
        LoggedInView view = new LoggedInView(vm);

        // View name should match constant in the view
        assertEquals("logged in", view.getViewName());

        // Wire actual existing controller setters with mocks
        view.setAchievementController(mock(AchievementController.class));
        view.setChangePasswordController(mock(ChangePasswordController.class));
        view.setProfileController(mock(ProfileController.class));
        view.setRankController(mock(RankController.class));
        view.setLogoutController(mock(LogoutController.class));
        view.setViewHistoryController(mock(ViewHistoryController.class));

        // Drive a property change to cover listener path
        PropertyChangeEvent evt = new PropertyChangeEvent(vm, "state", null, vm.getState());
        assertDoesNotThrow(() -> view.propertyChange(evt));
    }
}