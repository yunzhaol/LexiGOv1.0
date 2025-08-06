package interface_adapter;

import interface_adapter.rank.RankController;
import interface_adapter.rank.RankPresenter;
import interface_adapter.rank.RankState;
import interface_adapter.rank.RankViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.rank.RankInputBoundary;
import use_case.rank.RankInputData;
import use_case.rank.RankOutputData;
import use_case.rank.UserScore;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

class RankIATest {

    private RankInputBoundary mockInteractor;
    private RankController controller;
    private RankViewModel viewModel;
    private RankPresenter presenter;
    private TestListener listener;

    @BeforeEach
    void setUp() {
        mockInteractor = mock(RankInputBoundary.class);
        controller     = new RankController(mockInteractor);

        viewModel = new RankViewModel();
        presenter = new RankPresenter(viewModel);

        listener = new TestListener();
        viewModel.addPropertyChangeListener(listener);
    }

    // ------------------------------------------------------------
    // Controller → interactor delegation
    // ------------------------------------------------------------
    @Test
    void controllerDelegatesUsernameProperly() {
        String username = "alice";

        // Act
        controller.execute(username);

        // Assert
        var captor = forClass(RankInputData.class);
        verify(mockInteractor).execute(captor.capture());
        RankInputData actual = captor.getValue();

        assertEquals(username, actual.getUsername(),
                "Controller should wrap the username into RankInputData");
    }

    // ------------------------------------------------------------
    // Presenter success path
    // ------------------------------------------------------------
    @Test
    void presenterPrepareSuccessViewUpdatesStateAndFires() {
        // Arrange: sample leaderboard and fake output
        UserScore u1 = new UserScore(1, "alice", 100);
        UserScore u2 = new UserScore(2, "bob",   80);
        List<UserScore> leaderboard = List.of(u1, u2);

        RankOutputData out = mock(RankOutputData.class);
        when(out.currentUser()).thenReturn("bob");
        when(out.leaderboard()).thenReturn(leaderboard);
        when(out.myPosition()).thenReturn(2);

        // Act
        presenter.prepareSuccessView(out);

        // Assert: state fields
        RankState state = viewModel.getState();
        assertAll("Verify RankState after success",
                () -> assertEquals("bob", state.getCurrentUser()),
                () -> assertSame(leaderboard, state.getLeaderboard(),
                        "Presenter must install the same list instance"),
                () -> assertEquals(2, state.getPosition())
        );

        // Assert: event fired
        assertTrue(listener.fired, "Presenter must fire a property-change event");
    }

    // ------------------------------------------------------------
    // RankState defaults, getters and setters
    // ------------------------------------------------------------
    @Test
    void rankStateDefaultValuesAndSettersGetters() {
        RankState state = new RankState();

        // defaults
        assertAll("Default RankState",
                () -> assertNotNull(state.getLeaderboard()),
                () -> assertTrue(state.getLeaderboard().isEmpty()),
                () -> assertEquals("", state.getCurrentUser()),
                () -> assertEquals(0, state.getPosition())
        );

        // setters
        List<UserScore> dummy = List.of(new UserScore(1, "x", 1));
        state.setLeaderboard(dummy);
        state.setCurrentUser("x");
        state.setPosition(5);

        assertAll("After setters",
                () -> assertSame(dummy, state.getLeaderboard()),
                () -> assertEquals("x", state.getCurrentUser()),
                () -> assertEquals(5, state.getPosition())
        );
    }

    // ------------------------------------------------------------
    // RankViewModel initial wiring
    // ------------------------------------------------------------
    @Test
    void rankViewModelInitialStateAndViewName() {
        RankViewModel vm = new RankViewModel();
        assertAll("ViewModel initial state",
                () -> assertEquals("rank", vm.getViewName()),
                () -> assertNotNull(vm.getState(),
                        "ViewModel must start with a non-null RankState")
        );
    }

    // ------------------------------------------------------------
    // Helper listener to detect firePropertyChanged()
    // ------------------------------------------------------------
    private static class TestListener implements PropertyChangeListener {
        boolean fired = false;
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            fired = true;
        }
    }
}
