package interface_adapter;

import interface_adapter.view_history.ViewHistoryController;
import interface_adapter.view_history.ViewHistoryPresenter;
import interface_adapter.view_history.ViewHistoryState;
import interface_adapter.view_history.ViewHistoryViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.viewhistory.ViewHistoryInteractor;
import use_case.viewhistory.ViewHistoryOutputData;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ViewHistory feature, including controller and presenter behavior.
 */
public class ViewHistoryTest {

    /**
     * Mocked interactor for testing controller behavior.
     */
    private ViewHistoryInteractor viewHistoryInteractor;

    /**
     * Set up a new mock interactor before each test.
     */
    @BeforeEach
    public void setUp() {
        viewHistoryInteractor = mock(ViewHistoryInteractor.class);
    }

    /**
     * Test that the ViewHistoryController calls the interactor with the correct username.
     */
    @Test
    public void viewHistoryTest() {
        // Arrange
        ViewHistoryController viewHistoryController = new ViewHistoryController(viewHistoryInteractor);
        String username = "testUser";

        // Act
        viewHistoryController.execute(username);

        // Assert
        verify(viewHistoryInteractor, times(1)).execute(argThat(inputData ->
                inputData != null && username.equals(inputData.getUsername())
        ));
    }

    /**
     * Test the presenter successfully populates the ViewModel when valid data is provided.
     */
    @Test
    public void viewHistoryInputBoundaryTest() {
        ViewHistoryViewModel model = new ViewHistoryViewModel();
        ViewHistoryPresenter presenter = new ViewHistoryPresenter(model);

        // Simulate successful response from interactor
        presenter.prepareSuccessView(new ViewHistoryOutputData("a", new ArrayList<>(), 1, 0));

        ViewHistoryState viewHistoryState = model.getState();

        // Assert expected state values
        assertEquals(viewHistoryState.getSessions(), new ArrayList<>());
        assertEquals(viewHistoryState.getCurrentUser(), "a");
        assertEquals(viewHistoryState.getTotalWords(), 0);
        assertEquals(viewHistoryState.getErrorMessage(), null);
        assertEquals(viewHistoryState.getTotalSessions(), 1);
    }

    /**
     * Test presenter handles null sessions list without error and still updates the state.
     */
    @Test
    public void viewHistory2Test() {
        ViewHistoryViewModel model = new ViewHistoryViewModel();
        ViewHistoryPresenter presenter = new ViewHistoryPresenter(model);

        // Simulate response with null sessions list
        presenter.prepareSuccessView(new ViewHistoryOutputData("a", null, 1, 0));

        ViewHistoryState viewHistoryState = model.getState();

        // Even if sessions are null, default to empty list
        assertEquals(viewHistoryState.getSessions(), new ArrayList<>());
        assertEquals(viewHistoryState.getCurrentUser(), "a");
        assertEquals(viewHistoryState.getTotalWords(), 0);
        assertEquals(viewHistoryState.getErrorMessage(), null);
        assertEquals(viewHistoryState.getTotalSessions(), 1);
    }

    /**
     * Test presenter correctly sets an error message on failure.
     */
    @Test
    public void viewHistory3Test() {
        ViewHistoryViewModel model = new ViewHistoryViewModel();
        ViewHistoryPresenter presenter = new ViewHistoryPresenter(model);

        // Simulate failure with an error message
        presenter.prepareFailView("111");

        ViewHistoryState viewHistoryState = model.getState();

        // Assert error message is correctly passed to the state
        assertEquals(viewHistoryState.getErrorMessage(), "111");
    }
}
