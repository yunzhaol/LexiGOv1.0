package interface_adapter;

import entity.Achievement;
import interface_adapter.achievement.AchievementController;
import interface_adapter.achievement.AchievementPresenter;
import interface_adapter.achievement.AchievementState;
import interface_adapter.achievement.AchievementViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.achievement.AchievementInputBoundary;
import use_case.achievement.AchievementOutputData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Provides full coverage for the Controller, Presenter, ViewModel,
 * and State under interface_adapter.achievement.
 */
public class AchievementIATest {

    private AchievementInputBoundary mockInteractor;
    private AchievementViewModel viewModel;
    private TestListener listener;

    @BeforeEach
    void setup() {
        mockInteractor = mock(AchievementInputBoundary.class);
        viewModel = new AchievementViewModel();
        // Listen for propertyChanged events to verify that firePropertyChanged works
        listener = new TestListener();
        viewModel.addPropertyChangeListener(listener);
    }

    // ----------------------------------------
    // Controller tests
    // ----------------------------------------
    @Test
    void controllerCallsInteractorWithCorrectInput() {
        AchievementController controller = new AchievementController(mockInteractor);
        String testUser = "user123";

        controller.showAchievements(testUser);

        // Verify that interactor.evaluate(new AchievementInputData("user123")) was called
        verify(mockInteractor, times(1))
                .evaluate(argThat(input ->
                        input != null && testUser.equals(input.getUsername())
                ));
    }

    // ----------------------------------------
    // ViewModel / State initial state
    // ----------------------------------------
    @Test
    void viewModelInitialStateIsEmpty() {
        AchievementState state = viewModel.getState();
        // Upon creation, unlockedAchievements should be an empty list
        assertNotNull(state.getUnlockedAchievements());
        assertTrue(state.getUnlockedAchievements().isEmpty());
    }

    @Test
    void stateSetterAndGetterWork() {
        AchievementState state = viewModel.getState();
        List<String> dummy = Arrays.asList("a", "b");
        state.setUnlockedAchievements(dummy);
        assertSame(dummy, state.getUnlockedAchievements());
    }

    // ----------------------------------------
    // Presenter: success branch
    // ----------------------------------------
    @Test
    void presenterPopulatesViewModelWithFormattedMessages() {
        AchievementViewModel vm = new AchievementViewModel();
        AchievementPresenter presenter = new AchievementPresenter(vm);

        // Mock two Achievement instances
        Achievement a1 = mock(Achievement.class);
        when(a1.getIconUnicode()).thenReturn("★");
        when(a1.getDescription()).thenReturn("First");

        Achievement a2 = mock(Achievement.class);
        when(a2.getIconUnicode()).thenReturn("⚡");
        when(a2.getDescription()).thenReturn("Second");

        AchievementOutputData output = mock(AchievementOutputData.class);
        when(output.getUnlockedAchievements()).thenReturn(List.of(a1, a2));

        presenter.present(output);

        List<String> got = vm.getState().getUnlockedAchievements();
        assertEquals(List.of("★ First", "⚡ Second"), got);
    }

    // ----------------------------------------
    // Presenter: empty-list branch
    // ----------------------------------------
    @Test
    void presenterHandlesEmptyList() {
        AchievementPresenter presenter = new AchievementPresenter(viewModel);

        AchievementOutputData mockOutput = mock(AchievementOutputData.class);
        when(mockOutput.getUnlockedAchievements()).thenReturn(Collections.emptyList());

        presenter.present(mockOutput);

        assertTrue(viewModel.getState().getUnlockedAchievements().isEmpty());
    }

    // ----------------------------------------
    // Helper: mock listener to detect firePropertyChanged invocation
    // ----------------------------------------
    private static class TestListener implements PropertyChangeListener {
        boolean fired = false;
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            fired = true;
        }
    }

    // ----------------------------------------
    // Local interface to mock use_case.achievement.Achievement more easily with Mockito
    // ----------------------------------------
    private interface MockAchievement {
        String getIconUnicode();
        String getDescription();
    }
}
