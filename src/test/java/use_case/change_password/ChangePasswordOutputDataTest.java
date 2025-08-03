package use_case.change_password;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ChangePasswordOutputData
 */
class ChangePasswordOutputDataTest {

    @Test
    @DisplayName("Should create output data with all fields")
    void shouldCreateOutputDataWithAllFields() {
        // Given
        String username = "testUser";
        boolean needVerified = true;
        String securityQuestion = "What is your favorite color?";

        // When
        ChangePasswordOutputData outputData = new ChangePasswordOutputData(username, needVerified, securityQuestion);

        // Then
        assertEquals(username, outputData.getUsername());
        assertTrue(outputData.isNeedVerified());
        assertEquals(securityQuestion, outputData.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should handle null username")
    void shouldHandleNullUsername() {
        // When
        ChangePasswordOutputData outputData = new ChangePasswordOutputData(null, true, "Question?");

        // Then
        assertNull(outputData.getUsername());
        assertTrue(outputData.isNeedVerified());
    }

    @Test
    @DisplayName("Should handle empty username")
    void shouldHandleEmptyUsername() {
        // Given
        String emptyUsername = "";

        // When
        ChangePasswordOutputData outputData = new ChangePasswordOutputData(emptyUsername, false, "Question?");

        // Then
        assertEquals(emptyUsername, outputData.getUsername());
        assertFalse(outputData.isNeedVerified());
    }

    @Test
    @DisplayName("Should handle false needVerified flag")
    void shouldHandleFalseNeedVerifiedFlag() {
        // When
        ChangePasswordOutputData outputData = new ChangePasswordOutputData("user", false, null);

        // Then
        assertEquals("user", outputData.getUsername());
        assertFalse(outputData.isNeedVerified());
        assertNull(outputData.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should handle true needVerified flag")
    void shouldHandleTrueNeedVerifiedFlag() {
        // When
        ChangePasswordOutputData outputData = new ChangePasswordOutputData("user", true, "Question?");

        // Then
        assertEquals("user", outputData.getUsername());
        assertTrue(outputData.isNeedVerified());
        assertEquals("Question?", outputData.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should handle null security question")
    void shouldHandleNullSecurityQuestion() {
        // When
        ChangePasswordOutputData outputData = new ChangePasswordOutputData("user", false, null);

        // Then
        assertEquals("user", outputData.getUsername());
        assertFalse(outputData.isNeedVerified());
        assertNull(outputData.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should handle empty security question")
    void shouldHandleEmptySecurityQuestion() {
        // Given
        String emptyQuestion = "";

        // When
        ChangePasswordOutputData outputData = new ChangePasswordOutputData("user", true, emptyQuestion);

        // Then
        assertEquals("user", outputData.getUsername());
        assertTrue(outputData.isNeedVerified());
        assertEquals(emptyQuestion, outputData.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should handle long security question")
    void shouldHandleLongSecurityQuestion() {
        // Given
        String longQuestion = "What is the name of your first pet that you had when you were a child growing up in your hometown?".repeat(10);

        // When
        ChangePasswordOutputData outputData = new ChangePasswordOutputData("user", true, longQuestion);

        // Then
        assertEquals("user", outputData.getUsername());
        assertTrue(outputData.isNeedVerified());
        assertEquals(longQuestion, outputData.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should create multiple instances with different values")
    void shouldCreateMultipleInstancesWithDifferentValues() {
        // Given
        ChangePasswordOutputData outputData1 = new ChangePasswordOutputData("user1", true, "Question1?");
        ChangePasswordOutputData outputData2 = new ChangePasswordOutputData("user2", false, "Question2?");

        // Then
        assertNotEquals(outputData1.getUsername(), outputData2.getUsername());
        assertNotEquals(outputData1.isNeedVerified(), outputData2.isNeedVerified());
        assertNotEquals(outputData1.getSecurityQuestion(), outputData2.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should handle security user scenario")
    void shouldHandleSecurityUserScenario() {
        // Given - typical security user scenario
        String username = "securityUser";
        boolean needVerified = true;
        String securityQuestion = "What is your mother's maiden name?";

        // When
        ChangePasswordOutputData outputData = new ChangePasswordOutputData(username, needVerified, securityQuestion);

        // Then
        assertEquals(username, outputData.getUsername());
        assertTrue(outputData.isNeedVerified());
        assertEquals(securityQuestion, outputData.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should handle common user scenario")
    void shouldHandleCommonUserScenario() {
        // Given - typical common user scenario
        String username = "commonUser";
        boolean needVerified = false;
        String securityQuestion = null;

        // When
        ChangePasswordOutputData outputData = new ChangePasswordOutputData(username, needVerified, securityQuestion);

        // Then
        assertEquals(username, outputData.getUsername());
        assertFalse(outputData.isNeedVerified());
        assertNull(outputData.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should handle special characters in security question")
    void shouldHandleSpecialCharactersInSecurityQuestion() {
        // Given
        String questionWithSpecialChars = "What is your favorite symbol: @#$%^&*()?";

        // When
        ChangePasswordOutputData outputData = new ChangePasswordOutputData("user", true, questionWithSpecialChars);

        // Then
        assertEquals(questionWithSpecialChars, outputData.getSecurityQuestion());
    }
}