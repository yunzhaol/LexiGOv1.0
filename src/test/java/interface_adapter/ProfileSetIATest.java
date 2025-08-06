package interface_adapter;


import entity.Language;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;
import interface_adapter.profile.profile_set.ProfileSetController;
import interface_adapter.profile.profile_set.ProfileSetPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import use_case.profile.profile_set.ProfileSetInputBoundary;
import use_case.profile.profile_set.ProfileSetInputData;
import use_case.profile.profile_set.ProfileSetOutputData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileSetIATest {

    private ProfileSetInputBoundary mockInteractor;
    private ProfileSetController controller;
    private ProfileViewModel viewModel;
    private ProfileSetPresenter presenter;
    private TestListener listener;

    @BeforeEach
    void setUp() {
        mockInteractor = mock(ProfileSetInputBoundary.class);
        controller     = new ProfileSetController(mockInteractor);

        viewModel = new ProfileViewModel();
        presenter = new ProfileSetPresenter(viewModel);

        listener = new TestListener();
        viewModel.addPropertyChangeListener(listener);
    }

    @Test
    void controllerForwardsAllThreeArgsToUseCase() {
        String username  = "alice";
        Language newLang = Language.FR;
        Language oldLang = Language.EN;

        // Act
        controller.execute(username, newLang, oldLang);

        // Capture the actual argument
        @SuppressWarnings("unchecked")
        ArgumentCaptor<ProfileSetInputData> captor =
                ArgumentCaptor.forClass(ProfileSetInputData.class);

        verify(mockInteractor).execute(captor.capture());
        ProfileSetInputData actual = captor.getValue();

        // Now do plain JUnit assertions
        assertEquals(username,  actual.getUsername(),   "username should be forwarded");
        assertEquals(oldLang,   actual.getNewlanguage(), "new language should be forwarded");
        assertEquals(newLang,   actual.getOldlanguage(), "old language should be forwarded");
    }

    @Test
    void presenterPrepareSuccessViewUpdatesStateAndFiresEvent() {
        // Arrange
        ProfileSetOutputData out = mock(ProfileSetOutputData.class);
        when(out.getUsername()).thenReturn("bob");
        when(out.getNewlanguage()).thenReturn(Language.DE);

        // Act
        presenter.prepareSuccessView(out);

        // Assert
        assertTrue(listener.fired, "Should fire a property-change event");
        ProfileState s = viewModel.getState();
        assertEquals("",    s.getLanguageError(), "Error must be cleared on success");
        assertEquals("bob", s.getUsername());
        assertEquals(Language.DE, s.getOldlanguage());
    }

    @Test
    void presenterPrepareFailViewSetsErrorAndFiresEvent() {
        // Act
        presenter.prepareFailView("SOME_ERROR");

        // Assert
        assertTrue(listener.fired, "Should fire on failure too");
        ProfileState s = viewModel.getState();
        assertEquals("SOME_ERROR", s.getLanguageError());
    }

    private static class TestListener implements PropertyChangeListener {
        boolean fired = false;
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            fired = true;
        }
    }
}
