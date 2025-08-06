package view;

import interface_adapter.achievement.AchievementViewModel;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;

import static org.junit.jupiter.api.Assertions.*;

public class AchievementViewTest {

    @Test
    public void constructAndPropertyChange() {
        AchievementViewModel vm = new AchievementViewModel();
        AchievementView view = new AchievementView(vm);

        // getViewName should return a non-empty string (expected: "achievement")
        assertNotNull(view.getViewName());
        assertFalse(view.getViewName().trim().isEmpty());

        // Drive a simple property change cycle to touch the listener code
        PropertyChangeEvent evt = new PropertyChangeEvent(vm, "state", null, vm.getState());
        assertDoesNotThrow(() -> view.propertyChange(evt));
    }
}