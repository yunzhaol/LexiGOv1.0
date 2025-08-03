package use_case.change_password.make_password_change;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MakePasswordChangeOutputData
 */
class MakePasswordChangeOutputDataTest {

    @Test
    @DisplayName("Should create output data with error message")
    void shouldCreateOutputDataWithErrorMessage() {
        // Given
        String errorMessage = "Password cannot be empty";

        // When
        MakePasswordChangeOutputData outputData = new MakePasswordChangeOutputData(errorMessage);

        // Then
        assertEquals(errorMessage, outputData.getErrorMessage());
    }

    @Test
    @DisplayName("Should handle null error message")
    void shouldHandleNullErrorMessage() {
        // When
        MakePasswordChangeOutputData outputData = new MakePasswordChangeOutputData(null);

        // Then
        assertNull(outputData.getErrorMessage());
    }

    @Test
    @DisplayName("Should handle empty error message")
    void shouldHandleEmptyErrorMessage() {
        // Given
        String emptyMessage = "";

        // When
        MakePasswordChangeOutputData outputData = new MakePasswordChangeOutputData(emptyMessage);

        // Then
        assertEquals(emptyMessage, outputData.getErrorMessage());
    }

    @Test
    @DisplayName("Should handle whitespace error message")
    void shouldHandleWhitespaceErrorMessage() {
        // Given
        String whitespaceMessage = "   ";

        // When
        MakePasswordChangeOutputData outputData = new MakePasswordChangeOutputData(whitespaceMessage);

        // Then
        assertEquals(whitespaceMessage, outputData.getErrorMessage());
    }

    @Test
    @DisplayName("Should handle long error message")
    void shouldHandleLongErrorMessage() {
        // Given
        String longMessage = "This is a very long error message that contains multiple sentences and detailed information about what went wrong during the password change process.".repeat(10);

        // When
        MakePasswordChangeOutputData outputData = new MakePasswordChangeOutputData(longMessage);

        // Then
        assertEquals(longMessage, outputData.getErrorMessage());
    }

    @Test
    @DisplayName("Should handle error message with special characters")
    void shouldHandleErrorMessageWithSpecialCharacters() {
        // Given
        String specialMessage = "Error: Password cannot contain @#$%^&*()!";

        // When
        MakePasswordChangeOutputData outputData = new MakePasswordChangeOutputData(specialMessage);

        // Then
        assertEquals(specialMessage, outputData.getErrorMessage());
    }

    @Test
    @DisplayName("Should handle error message with line breaks")
    void shouldHandleErrorMessageWithLineBreaks() {
        // Given
        String messageWithLineBreaks = "Password validation failed:\n- Too short\n- Missing special characters";

        // When
        MakePasswordChangeOutputData outputData = new MakePasswordChangeOutputData(messageWithLineBreaks);

        // Then
        assertEquals(messageWithLineBreaks, outputData.getErrorMessage());
    }

    @Test
    @DisplayName("Should handle Unicode error message")
    void shouldHandleUnicodeErrorMessage() {
        // Given
        String unicodeMessage = "密码不能为空";

        // When
        MakePasswordChangeOutputData outputData = new MakePasswordChangeOutputData(unicodeMessage);

        // Then
        assertEquals(unicodeMessage, outputData.getErrorMessage());
    }

    @Test
    @DisplayName("Should create multiple instances with different messages")
    void shouldCreateMultipleInstancesWithDifferentMessages() {
        // Given
        MakePasswordChangeOutputData outputData1 = new MakePasswordChangeOutputData("Error 1");
        MakePasswordChangeOutputData outputData2 = new MakePasswordChangeOutputData("Error 2");

        // Then
        assertNotEquals(outputData1.getErrorMessage(), outputData2.getErrorMessage());
        assertEquals("Error 1", outputData1.getErrorMessage());
        assertEquals("Error 2", outputData2.getErrorMessage());
    }

    @Test
    @DisplayName("Should handle numeric error message")
    void shouldHandleNumericErrorMessage() {
        // Given
        String numericMessage = "Error code: 12345";

        // When
        MakePasswordChangeOutputData outputData = new MakePasswordChangeOutputData(numericMessage);

        // Then
        assertEquals(numericMessage, outputData.getErrorMessage());
    }

    @Test
    @DisplayName("Should handle HTML-like error message")
    void shouldHandleHtmlLikeErrorMessage() {
        // Given
        String htmlMessage = "<error>Password cannot be empty</error>";

        // When
        MakePasswordChangeOutputData outputData = new MakePasswordChangeOutputData(htmlMessage);

        // Then
        assertEquals(htmlMessage, outputData.getErrorMessage());
    }

    @Test
    @DisplayName("Should handle JSON-like error message")
    void shouldHandleJsonLikeErrorMessage() {
        // Given
        String jsonMessage = "{\"error\": \"Password validation failed\", \"code\": 400}";

        // When
        MakePasswordChangeOutputData outputData = new MakePasswordChangeOutputData(jsonMessage);

        // Then
        assertEquals(jsonMessage, outputData.getErrorMessage());
    }

    @Test
    @DisplayName("Should handle case-sensitive error messages")
    void shouldHandleCaseSensitiveErrorMessages() {
        // Given
        MakePasswordChangeOutputData upperCase = new MakePasswordChangeOutputData("PASSWORD ERROR");
        MakePasswordChangeOutputData lowerCase = new MakePasswordChangeOutputData("password error");

        // Then
        assertNotEquals(upperCase.getErrorMessage(), lowerCase.getErrorMessage());
    }

    @Test
    @DisplayName("Should handle common error messages")
    void shouldHandleCommonErrorMessages() {
        // Test various common error scenarios
        String[] commonErrors = {
                "Password cannot be empty",
                "Invalid security answer",
                "User not found",
                "Permission denied",
                "Database connection failed",
                "Invalid input format"
        };

        for (String error : commonErrors) {
            MakePasswordChangeOutputData outputData = new MakePasswordChangeOutputData(error);
            assertEquals(error, outputData.getErrorMessage());
        }
    }

    @Test
    @DisplayName("Should handle error message with tabs and spaces")
    void shouldHandleErrorMessageWithTabsAndSpaces() {
        // Given
        String messageWithTabs = "Error:\t\tPassword\t\tcannot\t\tbe\t\tempty";

        // When
        MakePasswordChangeOutputData outputData = new MakePasswordChangeOutputData(messageWithTabs);

        // Then
        assertEquals(messageWithTabs, outputData.getErrorMessage());
    }
}