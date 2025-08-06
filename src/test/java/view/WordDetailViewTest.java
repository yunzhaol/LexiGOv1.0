package view;

import interface_adapter.studysession.word_detail.WordDetailViewModel;
import interface_adapter.studysession.word_detail.WordDetailController;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class WordDetailViewTest {

    @Test
    public void constructSetControllerAndPropertyChange() {
        WordDetailViewModel vm = new WordDetailViewModel();
        WordDetailView view = new WordDetailView(vm);
        view.setWordDetailController(mock(WordDetailController.class));

        assertEquals("word detail", view.getViewName());

        PropertyChangeEvent evt = new PropertyChangeEvent(vm, "state", null, vm.getState());
        assertDoesNotThrow(() -> view.propertyChange(evt));
    }
}