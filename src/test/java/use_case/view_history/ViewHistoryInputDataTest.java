package use_case.view_history;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import use_case.viewhistory.ViewHistoryInputData;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ViewHistoryInputData
 */
class ViewHistoryInputDataTest {

    @Test
    @DisplayName("Should create input data with username")
    void shouldCreateInputDataWithUsername() {
        // Given
        String username = "testUser";

        // When
        ViewHistoryInputData inputData = new ViewHistoryInputData(username);

        // Then
        assertEquals(username, inputData.getUsername());
    }

    @Test
    @DisplayName("Should handle null username")
    void shouldHandleNullUsername() {
        // When
        ViewHistoryInputData inputData = new ViewHistoryInputData(null);

        // Then
        assertNull(inputData.getUsername());
    }

    @Test
    @DisplayName("Should handle empty username")
    void shouldHandleEmptyUsername() {
        // Given
        String emptyUsername = "";

        // When
        ViewHistoryInputData inputData = new ViewHistoryInputData(emptyUsername);

        // Then
        assertEquals(emptyUsername, inputData.getUsername());
    }

    @Test
    @DisplayName("Should handle whitespace username")
    void shouldHandleWhitespaceUsername() {
        // Given
        String whitespaceUsername = "   ";

        // When
        ViewHistoryInputData inputData = new ViewHistoryInputData(whitespaceUsername);

        // Then
        assertEquals(whitespaceUsername, inputData.getUsername());
    }

    @Test
    @DisplayName("Should handle special characters in username")
    void shouldHandleSpecialCharactersInUsername() {
        // Given
        String specialUsername = "user@test.com";

        // When
        ViewHistoryInputData inputData = new ViewHistoryInputData(specialUsername);

        // Then
        assertEquals(specialUsername, inputData.getUsername());
    }

    @Test
    @DisplayName("Should handle long username")
    void shouldHandleLongUsername() {
        // Given
        String longUsername = "a".repeat(1000);

        // When
        ViewHistoryInputData inputData = new ViewHistoryInputData(longUsername);

        // Then
        assertEquals(longUsername, inputData.getUsername());
        assertEquals(1000, inputData.getUsername().length());
    }
}