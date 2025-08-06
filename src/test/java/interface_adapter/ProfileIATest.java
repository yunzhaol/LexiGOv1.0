package interface_adapter;

import interface_adapter.profile.ProfileController;
import interface_adapter.profile.ProfilePresenter;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.profile.ProfileInputBoundary;
import use_case.profile.ProfileInputData;
import use_case.profile.ProfileOutputData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import entity.Language;

class ProfileIATest {

    private ProfileInputBoundary mockInteractor;
    private ProfileController controller;

    private ProfileViewModel viewModel;
    private TestListener listener;

    @BeforeEach
    void setUp() {
        // 1) mock the use‐case interactor
        mockInteractor = mock(ProfileInputBoundary.class);
        controller     = new ProfileController(mockInteractor);

        // 2) real view‐model + listener
        viewModel = new ProfileViewModel();
        listener  = new TestListener();
        viewModel.addPropertyChangeListener(listener);
    }

    // ------------------------------------------------------------
    // Controller → interactor delegation
    // ------------------------------------------------------------
    @Test
    void execute_forwardsUsernameToInteractor() {
        String username = "charlie";
        controller.execute(username);

        verify(mockInteractor, times(1))
                .execute(argThat((ProfileInputData in) ->
                        in != null && username.equals(in.getUsername())
                ));
    }

    // ------------------------------------------------------------
    // Presenter success path
    // ------------------------------------------------------------
    @Test
    void prepareSuccessView_updatesStateAndFires() {
        ProfilePresenter presenter = new ProfilePresenter(viewModel);

        // prepare fake output
        Language oldLang = Language.EN;
        Language[] langs = new Language[]{Language.FR, Language.ES};
        ProfileOutputData output = new ProfileOutputData(
                "deltaUser", oldLang, langs
        );

        // act
        presenter.prepareSuccessView(output);

        // assert: state updated
        ProfileState state = viewModel.getState();
        assertEquals("", state.getLanguageError(), "error should be cleared first");
        assertEquals("deltaUser", state.getUsername());
        assertSame(oldLang, state.getOldlanguage());
        assertArrayEquals(langs, state.getLanguages());

        // assert: change event fired
        assertTrue(listener.fired);
    }

    // ------------------------------------------------------------
    // ProfileState getters/setters & constructors
    // ------------------------------------------------------------
    @Test
    void profileState_defaultConstructorAndSettersGetters() {
        ProfileState s = new ProfileState();
        assertEquals("", s.getUsername());
        assertNull(s.getLanguageError());
        assertNull(s.getOldlanguage());
        assertNull(s.getLanguages());

        // exercise all setters
        Language langA = Language.DE;
        Language[] arr = new Language[]{langA};
        s.setUsername("echo");
        s.setLanguageError("err");
        s.setOldlanguage(langA);
        s.setLanguages(arr);

        assertEquals("echo", s.getUsername());
        assertEquals("err", s.getLanguageError());
        assertSame(langA, s.getOldlanguage());
        assertArrayEquals(arr, s.getLanguages());
    }

    @Test
    void profileState_fullArgConstructor() {
        Language a = Language.ZH;
        Language b = Language.JP;
        Language[] list = new Language[]{a, b};
        ProfileState s = new ProfileState(
                "foxtrot", "langErr", a, list
        );
        assertEquals("foxtrot", s.getUsername());
        assertEquals("langErr", s.getLanguageError());
        assertSame(a, s.getOldlanguage());
        assertArrayEquals(list, s.getLanguages());
    }

    // ------------------------------------------------------------
    // ProfileViewModel initial values
    // ------------------------------------------------------------
    @Test
    void viewModel_initialStateAndViewName() {
        ProfileViewModel vm = new ProfileViewModel();
        assertEquals("profile", vm.getViewName());
        assertNotNull(vm.getState());
    }

    // ------------------------------------------------------------
    // Helper listener to catch firePropertyChanged()
    // ------------------------------------------------------------
    private static class TestListener implements PropertyChangeListener {
        boolean fired = false;
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            fired = true;
        }
    }
}


