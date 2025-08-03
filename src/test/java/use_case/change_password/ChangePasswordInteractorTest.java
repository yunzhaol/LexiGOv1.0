package use_case.change_password;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for ChangePasswordInteractor
 */
class ChangePasswordInteractorTest {

    @Mock
    private ChangePasswordOutputBoundary mockPresenter;

    @Mock
    private ChangePasswordUserTypeDataAccessInterface mockDataAccess;

    private ChangePasswordInteractor interactor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        interactor = new ChangePasswordInteractor(mockPresenter, mockDataAccess);
    }

    @Test
    @DisplayName("Should handle SECURITY user type successfully")
    void shouldHandleSecurityUserTypeSuccessfully() {
        // Given
        String username = "securityUser";
        String securityQuestion = "What is your favorite color?";
        ChangePasswordInputData inputData = new ChangePasswordInputData(username);

        when(mockDataAccess.getType(username)).thenReturn("SECURITY");
        when(mockDataAccess.getSecurityQuestion(username)).thenReturn(securityQuestion);

        // When
        interactor.execute(inputData);

        // Then
        verify(mockDataAccess).getType(username);
        verify(mockDataAccess).getSecurityQuestion(username);

        ArgumentCaptor<ChangePasswordOutputData> outputCaptor = ArgumentCaptor.forClass(ChangePasswordOutputData.class);
        verify(mockPresenter).preparePage(outputCaptor.capture());

        ChangePasswordOutputData capturedOutput = outputCaptor.getValue();
        assertEquals(username, capturedOutput.getUsername());
        assertTrue(capturedOutput.isNeedVerified());
        assertEquals(securityQuestion, capturedOutput.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should handle COMMON user type")
    void shouldHandleCommonUserType() {
        // Given
        String username = "commonUser";
        ChangePasswordInputData inputData = new ChangePasswordInputData(username);

        when(mockDataAccess.getType(username)).thenReturn("COMMON");

        // When
        interactor.execute(inputData);

        // Then
        verify(mockDataAccess).getType(username);
        verify(mockDataAccess, never()).getSecurityQuestion(any());

        ArgumentCaptor<ChangePasswordOutputData> outputCaptor = ArgumentCaptor.forClass(ChangePasswordOutputData.class);
        verify(mockPresenter).preparePage(outputCaptor.capture());

        ChangePasswordOutputData capturedOutput = outputCaptor.getValue();
        assertEquals(username, capturedOutput.getUsername());
        assertFalse(capturedOutput.isNeedVerified());
        assertNull(capturedOutput.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should handle unknown user type as default case")
    void shouldHandleUnknownUserTypeAsDefaultCase() {
        // Given
        String username = "unknownUser";
        ChangePasswordInputData inputData = new ChangePasswordInputData(username);

        when(mockDataAccess.getType(username)).thenReturn("UNKNOWN");

        // When
        interactor.execute(inputData);

        // Then
        verify(mockDataAccess).getType(username);
        verify(mockDataAccess, never()).getSecurityQuestion(any());

        ArgumentCaptor<ChangePasswordOutputData> outputCaptor = ArgumentCaptor.forClass(ChangePasswordOutputData.class);
        verify(mockPresenter).preparePage(outputCaptor.capture());

        ChangePasswordOutputData capturedOutput = outputCaptor.getValue();
        assertEquals(username, capturedOutput.getUsername());
        assertFalse(capturedOutput.isNeedVerified());
        assertNull(capturedOutput.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should handle null user type")
    void shouldHandleNullUserType() {
        // Given
        String username = "nullTypeUser";
        ChangePasswordInputData inputData = new ChangePasswordInputData(username);

        when(mockDataAccess.getType(username)).thenReturn(null);

        // When
        interactor.execute(inputData);

        // Then
        verify(mockDataAccess).getType(username);
        verify(mockDataAccess, never()).getSecurityQuestion(any());

        ArgumentCaptor<ChangePasswordOutputData> outputCaptor = ArgumentCaptor.forClass(ChangePasswordOutputData.class);
        verify(mockPresenter).preparePage(outputCaptor.capture());

        ChangePasswordOutputData capturedOutput = outputCaptor.getValue();
        assertEquals(username, capturedOutput.getUsername());
        assertFalse(capturedOutput.isNeedVerified());
        assertNull(capturedOutput.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should handle empty string user type")
    void shouldHandleEmptyStringUserType() {
        // Given
        String username = "emptyTypeUser";
        ChangePasswordInputData inputData = new ChangePasswordInputData(username);

        when(mockDataAccess.getType(username)).thenReturn("");

        // When
        interactor.execute(inputData);

        // Then
        verify(mockDataAccess).getType(username);
        verify(mockDataAccess, never()).getSecurityQuestion(any());

        ArgumentCaptor<ChangePasswordOutputData> outputCaptor = ArgumentCaptor.forClass(ChangePasswordOutputData.class);
        verify(mockPresenter).preparePage(outputCaptor.capture());

        ChangePasswordOutputData capturedOutput = outputCaptor.getValue();
        assertEquals(username, capturedOutput.getUsername());
        assertFalse(capturedOutput.isNeedVerified());
        assertNull(capturedOutput.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should handle SECURITY user with null security question")
    void shouldHandleSecurityUserWithNullSecurityQuestion() {
        // Given
        String username = "securityUser";
        ChangePasswordInputData inputData = new ChangePasswordInputData(username);

        when(mockDataAccess.getType(username)).thenReturn("SECURITY");
        when(mockDataAccess.getSecurityQuestion(username)).thenReturn(null);

        // When
        interactor.execute(inputData);

        // Then
        verify(mockDataAccess).getType(username);
        verify(mockDataAccess).getSecurityQuestion(username);

        ArgumentCaptor<ChangePasswordOutputData> outputCaptor = ArgumentCaptor.forClass(ChangePasswordOutputData.class);
        verify(mockPresenter).preparePage(outputCaptor.capture());

        ChangePasswordOutputData capturedOutput = outputCaptor.getValue();
        assertEquals(username, capturedOutput.getUsername());
        assertTrue(capturedOutput.isNeedVerified());
        assertNull(capturedOutput.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should handle SECURITY user with empty security question")
    void shouldHandleSecurityUserWithEmptySecurityQuestion() {
        // Given
        String username = "securityUser";
        String emptyQuestion = "";
        ChangePasswordInputData inputData = new ChangePasswordInputData(username);

        when(mockDataAccess.getType(username)).thenReturn("SECURITY");
        when(mockDataAccess.getSecurityQuestion(username)).thenReturn(emptyQuestion);

        // When
        interactor.execute(inputData);

        // Then
        verify(mockDataAccess).getType(username);
        verify(mockDataAccess).getSecurityQuestion(username);

        ArgumentCaptor<ChangePasswordOutputData> outputCaptor = ArgumentCaptor.forClass(ChangePasswordOutputData.class);
        verify(mockPresenter).preparePage(outputCaptor.capture());

        ChangePasswordOutputData capturedOutput = outputCaptor.getValue();
        assertEquals(username, capturedOutput.getUsername());
        assertTrue(capturedOutput.isNeedVerified());
        assertEquals(emptyQuestion, capturedOutput.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should handle null username in input data")
    void shouldHandleNullUsernameInInputData() {
        // Given
        ChangePasswordInputData inputData = new ChangePasswordInputData(null);

        when(mockDataAccess.getType(null)).thenReturn("COMMON");

        // When
        interactor.execute(inputData);

        // Then
        verify(mockDataAccess).getType(null);

        ArgumentCaptor<ChangePasswordOutputData> outputCaptor = ArgumentCaptor.forClass(ChangePasswordOutputData.class);
        verify(mockPresenter).preparePage(outputCaptor.capture());

        ChangePasswordOutputData capturedOutput = outputCaptor.getValue();
        assertNull(capturedOutput.getUsername());
        assertFalse(capturedOutput.isNeedVerified());
        assertNull(capturedOutput.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should handle case-sensitive user type")
    void shouldHandleCaseSensitiveUserType() {
        // Given
        String username = "caseTestUser";
        ChangePasswordInputData inputData = new ChangePasswordInputData(username);

        when(mockDataAccess.getType(username)).thenReturn("security"); // lowercase

        // When
        interactor.execute(inputData);

        // Then - should go to default case since it's not "SECURITY"
        verify(mockDataAccess).getType(username);
        verify(mockDataAccess, never()).getSecurityQuestion(any());

        ArgumentCaptor<ChangePasswordOutputData> outputCaptor = ArgumentCaptor.forClass(ChangePasswordOutputData.class);
        verify(mockPresenter).preparePage(outputCaptor.capture());

        ChangePasswordOutputData capturedOutput = outputCaptor.getValue();
        assertEquals(username, capturedOutput.getUsername());
        assertFalse(capturedOutput.isNeedVerified());
        assertNull(capturedOutput.getSecurityQuestion());
    }

    @Test
    @DisplayName("Should handle data access exception gracefully")
    void shouldHandleDataAccessExceptionGracefully() {
        // Given
        String username = "exceptionUser";
        ChangePasswordInputData inputData = new ChangePasswordInputData(username);

        when(mockDataAccess.getType(username)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            interactor.execute(inputData);
        });

        verify(mockDataAccess).getType(username);
        verify(mockPresenter, never()).preparePage(any());
    }

    @Test
    @DisplayName("Should handle security question retrieval exception")
    void shouldHandleSecurityQuestionRetrievalException() {
        // Given
        String username = "securityUser";
        ChangePasswordInputData inputData = new ChangePasswordInputData(username);

        when(mockDataAccess.getType(username)).thenReturn("SECURITY");
        when(mockDataAccess.getSecurityQuestion(username)).thenThrow(new RuntimeException("Question retrieval error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            interactor.execute(inputData);
        });

        verify(mockDataAccess).getType(username);
        verify(mockDataAccess).getSecurityQuestion(username);
        verify(mockPresenter, never()).preparePage(any());
    }

    @Test
    @DisplayName("Should verify exact method call sequence")
    void shouldVerifyExactMethodCallSequence() {
        // Given
        String username = "testUser";
        String securityQuestion = "Test question?";
        ChangePasswordInputData inputData = new ChangePasswordInputData(username);

        when(mockDataAccess.getType(username)).thenReturn("SECURITY");
        when(mockDataAccess.getSecurityQuestion(username)).thenReturn(securityQuestion);

        // When
        interactor.execute(inputData);

        // Then - verify exact call sequence
        verify(mockDataAccess, times(1)).getType(username);
        verify(mockDataAccess, times(1)).getSecurityQuestion(username);
        verify(mockPresenter, times(1)).preparePage(any(ChangePasswordOutputData.class));

        verifyNoMoreInteractions(mockDataAccess);
        verifyNoMoreInteractions(mockPresenter);
    }
}