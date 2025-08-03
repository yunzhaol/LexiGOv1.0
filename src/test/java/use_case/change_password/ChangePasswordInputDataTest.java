package use_case.change_password;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ChangePasswordInputData
 */
class ChangePasswordInputDataTest {

    @Test
    @DisplayName("Should create input data with username")
    void shouldCreateInputDataWithUsername() {
        // Given
        String username = "testUser";

        // When
        ChangePasswordInputData inputData = new ChangePasswordInputData(username);

        // Then
        assertEquals(username, inputData.getUsername());
    }

    @Test
    @DisplayName("Should handle null username")
    void shouldHandleNullUsername() {
        // When
        ChangePasswordInputData inputData = new ChangePasswordInputData(null);

        // Then
        assertNull(inputData.getUsername());
    }

    @Test
    @DisplayName("Should handle empty username")
    void shouldHandleEmptyUsername() {
        // Given
        String emptyUsername = "";

        // When
        ChangePasswordInputData inputData = new ChangePasswordInputData(emptyUsername);

        // Then
        assertEquals(emptyUsername, inputData.getUsername());
    }

    @Test
    @DisplayName("Should handle whitespace username")
    void shouldHandleWhitespaceUsername() {
        // Given
        String whitespaceUsername = "   ";

        // When
        ChangePasswordInputData inputData = new ChangePasswordInputData(whitespaceUsername);

        // Then
        assertEquals(whitespaceUsername, inputData.getUsername());
    }

    @Test
    @DisplayName("Should handle special characters in username")
    void shouldHandleSpecialCharactersInUsername() {
        // Given
        String specialUsername = "user@test.com";

        // When
        ChangePasswordInputData inputData = new ChangePasswordInputData(specialUsername);

        // Then
        assertEquals(specialUsername, inputData.getUsername());
    }

    @Test
    @DisplayName("Should handle long username")
    void shouldHandleLongUsername() {
        // Given
        String longUsername = "a".repeat(1000);

        // When
        ChangePasswordInputData inputData = new ChangePasswordInputData(longUsername);

        // Then
        assertEquals(longUsername, inputData.getUsername());
        assertEquals(1000, inputData.getUsername().length());
    }

    @Test
    @DisplayName("Should create multiple instances with different usernames")
    void shouldCreateMultipleInstancesWithDifferentUsernames() {
        // Given
        ChangePasswordInputData inputData1 = new ChangePasswordInputData("user1");
        ChangePasswordInputData inputData2 = new ChangePasswordInputData("user2");

        // Then
        assertNotEquals(inputData1.getUsername(), inputData2.getUsername());
        assertEquals("user1", inputData1.getUsername());
        assertEquals("user2", inputData2.getUsername());
    }

    @Test
    @DisplayName("Should handle Unicode characters in username")
    void shouldHandleUnicodeCharactersInUsername() {
        // Given
        String unicodeUsername = "用户名测试";

        // When
        ChangePasswordInputData inputData = new ChangePasswordInputData(unicodeUsername);

        // Then
        assertEquals(unicodeUsername, inputData.getUsername());
    }
}