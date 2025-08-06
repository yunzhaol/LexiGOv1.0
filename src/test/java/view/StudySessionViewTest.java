package view;

import interface_adapter.studysession.StudySessionViewModel;
import interface_adapter.studysession.StudySessionController;
import interface_adapter.studysession.word_detail.WordDetailController;
import interface_adapter.finish.FinishController;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class StudySessionViewTest {

    @Test
    public void constructSetControllersAndPropertyChange() {
        StudySessionViewModel vm = new StudySessionViewModel();
        StudySessionView view = new StudySessionView(vm);

        // Wire the three controllers exposed by the view
        view.setStudySessionController(mock(StudySessionController.class));
        view.setFinishCheckInController(mock(FinishController.class));
        view.setWordDetailController(mock(WordDetailController.class));

        assertNotNull(view.getViewName());
        assertFalse(view.getViewName().trim().isEmpty());

        PropertyChangeEvent evt = new PropertyChangeEvent(vm, "state", null, vm.getState());
        assertDoesNotThrow(() -> view.propertyChange(evt));
    }
}