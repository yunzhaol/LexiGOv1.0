package interface_adapter;

import interface_adapter.session.LoggedInViewModel;
import interface_adapter.start_checkin.StartCheckInController;
import interface_adapter.start_checkin.StartCheckInPresenter;
import interface_adapter.start_checkin.StartCheckInState;
import interface_adapter.start_checkin.StartCheckInViewModel;
import interface_adapter.studysession.StudySessionController;
import interface_adapter.studysession.StudySessionViewModel;
import interface_adapter.view_history.ViewHistoryViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.start_checkin.StartCheckInInputData;
import use_case.start_checkin.StartCheckInInteractor;
import use_case.start_checkin.StartCheckInOutputData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the StartCheckIn feature including controller, presenter, and state logic.
 */
public class StartCheckInTest {

    /**
     * Mocked interactor used to test controller interactions.
     */
    private StartCheckInInteractor Interactor;

    /**
     * Sets up a fresh mock of StartCheckInInteractor before each test.
     */
    @BeforeEach
    public void setup() {
        Interactor = mock(StartCheckInInteractor.class);
    }

    /**
     * Tests that the StartCheckInController correctly passes input data to the interactor.
     */
    @Test
    public void startCheckIn1Test() {
        StartCheckInController controller = new StartCheckInController(Interactor);

        // Act: Execute the controller with test input
        controller.execute("1", "3");

        // Assert: Verify the interactor was called with the correct input data
        verify(Interactor, times(1)).execute(argThat(inputData ->
                inputData != null && "1".equals(inputData.getUsername()) && "3".equals(inputData.getLength()))
        );
    }

    /**
     * Tests that the presenter correctly updates the view model on success.
     */
    @Test
    public void startCheckIn2Test() {
        StartCheckInViewModel viewModel = new StartCheckInViewModel();
        StartCheckInPresenter presenter = new StartCheckInPresenter(
                new ViewManagerModel(), viewModel, new StudySessionViewModel(), new LoggedInViewModel());

        // Simulate a successful check-in
        presenter.prepareSuccessView(new StartCheckInOutputData("10", false, "aaa"));

        StartCheckInState state = viewModel.getState();

        // Assert: After success, certain values should be reset/cleared
        assertEquals(state.getNumberWords(), "");
        assertEquals(state.getUsername(), null);
    }

    /**
     * Tests that the presenter correctly updates the view model on failure.
     */
    @Test
    public void startCheckIn3Test() {
        StartCheckInViewModel viewModel = new StartCheckInViewModel();
        StartCheckInPresenter presenter = new StartCheckInPresenter(
                new ViewManagerModel(), viewModel, new StudySessionViewModel(), new LoggedInViewModel());

        // Simulate a failure with error message "aaa"
        presenter.prepareFailView("aaa");

        StartCheckInState state = viewModel.getState();

        // Assert: The input number error should be set correctly
        assertEquals(state.getInputNumberError(), "aaa");
    }

    /**
     * Tests the StartCheckInState's getter and setter for username.
     */
    @Test
    public void startCheckIn4Test() {
        StartCheckInState state = new StartCheckInState();

        // Set and then retrieve the username
        state.setUsername("111");

        // Assert: The value should match what was set
        assertEquals(state.getUsername(), "111");
    }
}
