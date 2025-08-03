package use_case.change_password;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import use_case.change_password.make_password_change.MakePasswordChangeInputData;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MakePasswordChangeInputData
 */
class MakePasswordChangeInputDataTest {

    @Test
    @DisplayName("Should create input data with all fields")
    void shouldCreateInputDataWithAllFields() {
        // Given
        String username = "testUser";
        String newPassword = "newPassword123";
        String securityAnswer = "blue";

        // When
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, newPassword, securityAnswer);

        // Then
        assertEquals(username, inputData.getUsername());
        assertEquals(newPassword, inputData.getNewPassword());
        assertEquals(securityAnswer, inputData.getSecurityAnswer());
    }

    @Test
    @DisplayName("Should handle null username")
    void shouldHandleNullUsername() {
        // When
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(null, "password", "answer");

        // Then
        assertNull(inputData.getUsername());
        assertEquals("password", inputData.getNewPassword());
        assertEquals("answer", inputData.getSecurityAnswer());
    }

    @Test
    @DisplayName("Should handle null new password")
    void shouldHandleNullNewPassword() {
        // When
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData("user", null, "answer");

        // Then
        assertEquals("user", inputData.getUsername());
        assertNull(inputData.getNewPassword());
        assertEquals("answer", inputData.getSecurityAnswer());
    }

    @Test
    @DisplayName("Should handle null security answer")
    void shouldHandleNullSecurityAnswer() {
        // When
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData("user", "password", null);

        // Then
        assertEquals("user", inputData.getUsername());
        assertEquals("password", inputData.getNewPassword());
        assertNull(inputData.getSecurityAnswer());
    }

    @Test
    @DisplayName("Should handle all null values")
    void shouldHandleAllNullValues() {
        // When
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(null, null, null);

        // Then
        assertNull(inputData.getUsername());
        assertNull(inputData.getNewPassword());
        assertNull(inputData.getSecurityAnswer());
    }

    @Test
    @DisplayName("Should handle empty strings")
    void shouldHandleEmptyStrings() {
        // When
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData("", "", "");

        // Then
        assertEquals("", inputData.getUsername());
        assertEquals("", inputData.getNewPassword());
        assertEquals("", inputData.getSecurityAnswer());
    }

    @Test
    @DisplayName("Should handle whitespace strings")
    void shouldHandleWhitespaceStrings() {
        // Given
        String whitespace = "   ";

        // When
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(whitespace, whitespace, whitespace);

        // Then
        assertEquals(whitespace, inputData.getUsername());
        assertEquals(whitespace, inputData.getNewPassword());
        assertEquals(whitespace, inputData.getSecurityAnswer());
    }

    @Test
    @DisplayName("Should handle long password")
    void shouldHandleLongPassword() {
        // Given
        String longPassword = "a".repeat(1000);

        // When
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData("user", longPassword, "answer");

        // Then
        assertEquals("user", inputData.getUsername());
        assertEquals(longPassword, inputData.getNewPassword());
        assertEquals(1000, inputData.getNewPassword().length());
    }

    @Test
    @DisplayName("Should handle special characters in password")
    void shouldHandleSpecialCharactersInPassword() {
        // Given
        String specialPassword = "P@ssw0rd!#$%^&*()";

        // When
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData("user", specialPassword, "answer");

        // Then
        assertEquals(specialPassword, inputData.getNewPassword());
    }

    @Test
    @DisplayName("Should handle long security answer")
    void shouldHandleLongSecurityAnswer() {
        // Given
        String longAnswer = "This is a very long security answer that contains multiple words and sentences.".repeat(10);

        // When
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData("user", "password", longAnswer);

        // Then
        assertEquals(longAnswer, inputData.getSecurityAnswer());
    }

    @Test
    @DisplayName("Should handle Unicode characters")
    void shouldHandleUnicodeCharacters() {
        // Given
        String unicodeUsername = "用户名";
        String unicodePassword = "密码123";
        String unicodeAnswer = "答案";

        // When
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(unicodeUsername, unicodePassword, unicodeAnswer);

        // Then
        assertEquals(unicodeUsername, inputData.getUsername());
        assertEquals(unicodePassword, inputData.getNewPassword());
        assertEquals(unicodeAnswer, inputData.getSecurityAnswer());
    }

    @Test
    @DisplayName("Should create multiple instances with different values")
    void shouldCreateMultipleInstancesWithDifferentValues() {
        // Given
        MakePasswordChangeInputData inputData1 = new MakePasswordChangeInputData("user1", "pass1", "answer1");
        MakePasswordChangeInputData inputData2 = new MakePasswordChangeInputData("user2", "pass2", "answer2");

        // Then
        assertNotEquals(inputData1.getUsername(), inputData2.getUsername());
        assertNotEquals(inputData1.getNewPassword(), inputData2.getNewPassword());
        assertNotEquals(inputData1.getSecurityAnswer(), inputData2.getSecurityAnswer());
    }

    @Test
    @DisplayName("Should handle case-sensitive values")
    void shouldHandleCaseSensitiveValues() {
        // Given
        MakePasswordChangeInputData inputData1 = new MakePasswordChangeInputData("User", "Password", "Answer");
        MakePasswordChangeInputData inputData2 = new MakePasswordChangeInputData("user", "password", "answer");

        // Then
        assertNotEquals(inputData1.getUsername(), inputData2.getUsername());
        assertNotEquals(inputData1.getNewPassword(), inputData2.getNewPassword());
        assertNotEquals(inputData1.getSecurityAnswer(), inputData2.getSecurityAnswer());
    }

    @Test
    @DisplayName("Should handle numeric values as strings")
    void shouldHandleNumericValuesAsStrings() {
        // Given
        String numericUsername = "12345";
        String numericPassword = null;
    }
}