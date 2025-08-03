package use_case.change_password.make_password_change;

import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for MakePasswordChangeInteractor
 */
class MakePasswordChangeInteractorTest {

    @Mock
    private MakePasswordChangeOutputBoundary mockPresenter;

    @Mock
    private UserPasswordDataAccessInterface mockUserDAO;

    @Mock
    private UserFactory mockCommonUserFactory;

    @Mock
    private UserFactory mockSecurityUserFactory;

    @Mock
    private User mockUser;

    @Mock
    private User mockNewCommonUser;

    @Mock
    private User mockNewSecurityUser;

    private MakePasswordChangeInteractor interactor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        interactor = new MakePasswordChangeInteractor(mockPresenter, mockUserDAO, mockSecurityUserFactory);
    }

    @Test
    @DisplayName("Should successfully change password for common user")
    void shouldSuccessfullyChangePasswordForCommonUser() {
        // Given
        String username = "commonUser";
        String newPassword = "newPassword123";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, newPassword, null);

        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockCommonUserFactory.create(username, newPassword)).thenReturn(mockNewCommonUser);

        // When
        interactor.make_password_change(inputData);

        // Then
        verify(mockUserDAO).get(username);
        verify(mockCommonUserFactory).create(username, newPassword);
        verify(mockUserDAO).update(username, mockNewCommonUser);
        verify(mockPresenter).presentSuccess();
        verify(mockPresenter, never()).presentFailure(any());
    }

    @Test
    @DisplayName("Should fail when common user password is null")
    void shouldFailWhenCommonUserPasswordIsNull() {
        // Given
        String username = "commonUser";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, null, null);

        when(mockUserDAO.get(username)).thenReturn(mockUser);

        // When
        interactor.make_password_change(inputData);

        // Then
        verify(mockUserDAO).get(username);
        verify(mockCommonUserFactory, never()).create(any(), any());
        verify(mockUserDAO, never()).update(any(), any());
        verify(mockPresenter, never()).presentSuccess();

        ArgumentCaptor<MakePasswordChangeOutputData> outputCaptor = ArgumentCaptor.forClass(MakePasswordChangeOutputData.class);
        verify(mockPresenter).presentFailure(outputCaptor.capture());

        MakePasswordChangeOutputData capturedOutput = outputCaptor.getValue();
        assertEquals("Password cannot be empty", capturedOutput.getErrorMessage());
    }

    @Test
    @DisplayName("Should successfully change password for security user with correct answer")
    void shouldSuccessfullyChangePasswordForSecurityUserWithCorrectAnswer() {
        // Given
        String username = "securityUser";
        String newPassword = "newPassword123";
        String securityAnswer = "correctAnswer";
        String securityQuestion = "What is your favorite color?";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, newPassword, securityAnswer);

        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockUserDAO.getAnswer(username)).thenReturn(securityAnswer);
        when(mockUserDAO.getQuestion(username)).thenReturn(securityQuestion);
        when(mockSecurityUserFactory.create(username, newPassword, securityQuestion, securityAnswer)).thenReturn(mockNewSecurityUser);

        // When
        interactor.make_password_change(inputData);

        // Then
        verify(mockUserDAO).get(username);
        verify(mockUserDAO).getAnswer(username);
        verify(mockUserDAO).getQuestion(username);
        verify(mockSecurityUserFactory).create(username, newPassword, securityQuestion, securityAnswer);
        verify(mockUserDAO).update(username, mockNewSecurityUser);
        verify(mockPresenter).presentSuccess();
        verify(mockPresenter, never()).presentFailure(any());
    }

    @Test
    @DisplayName("Should fail when security user password is null")
    void shouldFailWhenSecurityUserPasswordIsNull() {
        // Given
        String username = "securityUser";
        String securityAnswer = "correctAnswer";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, null, securityAnswer);

        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockUserDAO.getAnswer(username)).thenReturn(securityAnswer);

        // When
        interactor.make_password_change(inputData);

        // Then
        verify(mockUserDAO).get(username);
        verify(mockUserDAO).getAnswer(username);
        verify(mockUserDAO, never()).getQuestion(any());
        verify(mockSecurityUserFactory, never()).create(any(), any(), any(), any());
        verify(mockUserDAO, never()).update(any(), any());
        verify(mockPresenter, never()).presentSuccess();

        ArgumentCaptor<MakePasswordChangeOutputData> outputCaptor = ArgumentCaptor.forClass(MakePasswordChangeOutputData.class);
        verify(mockPresenter).presentFailure(outputCaptor.capture());

        MakePasswordChangeOutputData capturedOutput = outputCaptor.getValue();
        assertEquals("Password cannot be empty", capturedOutput.getErrorMessage());
    }

    @Test
    @DisplayName("Should not proceed when security answer is incorrect")
    void shouldNotProceedWhenSecurityAnswerIsIncorrect() {
        // Given
        String username = "securityUser";
        String newPassword = "newPassword123";
        String correctAnswer = "correctAnswer";
        String incorrectAnswer = "wrongAnswer";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, newPassword, incorrectAnswer);

        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockUserDAO.getAnswer(username)).thenReturn(correctAnswer);

        // When
        interactor.make_password_change(inputData);

        // Then
        verify(mockUserDAO).get(username);
        verify(mockUserDAO).getAnswer(username);
        verify(mockUserDAO, never()).getQuestion(any());
        verify(mockSecurityUserFactory, never()).create(any(), any(), any(), any());
        verify(mockUserDAO, never()).update(any(), any());
        verify(mockPresenter, never()).presentSuccess();
        verify(mockPresenter, never()).presentFailure(any());
    }

    @Test
    @DisplayName("Should handle null security answer from database")
    void shouldHandleNullSecurityAnswerFromDatabase() {
        // Given
        String username = "securityUser";
        String newPassword = "newPassword123";
        String securityAnswer = "someAnswer";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, newPassword, securityAnswer);

        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockUserDAO.getAnswer(username)).thenReturn(null);

        // When & Then - Should throw NullPointerException
        assertThrows(NullPointerException.class, () -> {
            interactor.make_password_change(inputData);
        });

        verify(mockUserDAO).get(username);
        verify(mockUserDAO).getAnswer(username);
    }

    @Test
    @DisplayName("Should handle empty string security answer")
    void shouldHandleEmptyStringSecurityAnswer() {
        // Given
        String username = "securityUser";
        String newPassword = "newPassword123";
        String emptyAnswer = "";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, newPassword, emptyAnswer);

        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockUserDAO.getAnswer(username)).thenReturn(emptyAnswer);
        when(mockUserDAO.getQuestion(username)).thenReturn("Question?");
        when(mockSecurityUserFactory.create(username, newPassword, "Question?", emptyAnswer)).thenReturn(mockNewSecurityUser);

        // When
        interactor.make_password_change(inputData);

        // Then
        verify(mockUserDAO).get(username);
        verify(mockUserDAO).getAnswer(username);
        verify(mockUserDAO).getQuestion(username);
        verify(mockSecurityUserFactory).create(username, newPassword, "Question?", emptyAnswer);
        verify(mockUserDAO).update(username, mockNewSecurityUser);
        verify(mockPresenter).presentSuccess();
    }

    @Test
    @DisplayName("Should handle case-sensitive security answer comparison")
    void shouldHandleCaseSensitiveSecurityAnswerComparison() {
        // Given
        String username = "securityUser";
        String newPassword = "newPassword123";
        String correctAnswer = "Blue";
        String incorrectCaseAnswer = "blue";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, newPassword, incorrectCaseAnswer);

        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockUserDAO.getAnswer(username)).thenReturn(correctAnswer);

        // When
        interactor.make_password_change(inputData);

        // Then - should not proceed due to case mismatch
        verify(mockUserDAO).get(username);
        verify(mockUserDAO).getAnswer(username);
        verify(mockUserDAO, never()).getQuestion(any());
        verify(mockSecurityUserFactory, never()).create(any(), any(), any(), any());
        verify(mockUserDAO, never()).update(any(), any());
        verify(mockPresenter, never()).presentSuccess();
        verify(mockPresenter, never()).presentFailure(any());
    }

    @Test
    @DisplayName("Should handle whitespace in security answer")
    void shouldHandleWhitespaceInSecurityAnswer() {
        // Given
        String username = "securityUser";
        String newPassword = "newPassword123";
        String answerWithSpaces = " blue ";
        String exactAnswer = " blue ";
        String securityQuestion = "What is your favorite color?";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, newPassword, answerWithSpaces);

        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockUserDAO.getAnswer(username)).thenReturn(exactAnswer);
        when(mockUserDAO.getQuestion(username)).thenReturn(securityQuestion);
        when(mockSecurityUserFactory.create(username, newPassword, securityQuestion, answerWithSpaces)).thenReturn(mockNewSecurityUser);

        // When
        interactor.make_password_change(inputData);

        // Then
        verify(mockUserDAO).get(username);
        verify(mockUserDAO).getAnswer(username);
        verify(mockUserDAO).getQuestion(username);
        verify(mockSecurityUserFactory).create(username, newPassword, securityQuestion, answerWithSpaces);
        verify(mockUserDAO).update(username, mockNewSecurityUser);
        verify(mockPresenter).presentSuccess();
    }

    @Test
    @DisplayName("Should handle null username")
    void shouldHandleNullUsername() {
        // Given
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(null, "password", null);

        when(mockUserDAO.get(null)).thenReturn(mockUser);
        when(mockCommonUserFactory.create(null, "password")).thenReturn(mockNewCommonUser);

        // When
        interactor.make_password_change(inputData);

        // Then
        verify(mockUserDAO).get(null);
        verify(mockCommonUserFactory).create(null, "password");
        verify(mockUserDAO).update(null, mockNewCommonUser);
        verify(mockPresenter).presentSuccess();
    }

    @Test
    @DisplayName("Should handle empty username")
    void shouldHandleEmptyUsername() {
        // Given
        String emptyUsername = "";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(emptyUsername, "password", null);

        when(mockUserDAO.get(emptyUsername)).thenReturn(mockUser);
        when(mockCommonUserFactory.create(emptyUsername, "password")).thenReturn(mockNewCommonUser);

        // When
        interactor.make_password_change(inputData);

        // Then
        verify(mockUserDAO).get(emptyUsername);
        verify(mockCommonUserFactory).create(emptyUsername, "password");
        verify(mockUserDAO).update(emptyUsername, mockNewCommonUser);
        verify(mockPresenter).presentSuccess();
    }

    @Test
    @DisplayName("Should handle user DAO returning null user")
    void shouldHandleUserDAOReturningNullUser() {
        // Given
        String username = "nonexistentUser";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, "password", null);

        when(mockUserDAO.get(username)).thenReturn(null);

        // When & Then - Should throw NullPointerException when trying to process null user
        assertThrows(NullPointerException.class, () -> {
            interactor.make_password_change(inputData);
        });

        verify(mockUserDAO).get(username);
    }

    @Test
    @DisplayName("Should handle database exception during user retrieval")
    void shouldHandleDatabaseExceptionDuringUserRetrieval() {
        // Given
        String username = "testUser";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, "password", null);

        when(mockUserDAO.get(username)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            interactor.make_password_change(inputData);
        });

        verify(mockUserDAO).get(username);
        verify(mockPresenter, never()).presentSuccess();
        verify(mockPresenter, never()).presentFailure(any());
    }

    @Test
    @DisplayName("Should handle database exception during user update")
    void shouldHandleDatabaseExceptionDuringUserUpdate() {
        // Given
        String username = "testUser";
        String newPassword = "newPassword";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, newPassword, null);

        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockCommonUserFactory.create(username, newPassword)).thenReturn(mockNewCommonUser);
        doThrow(new RuntimeException("Update failed")).when(mockUserDAO).update(username, mockNewCommonUser);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            interactor.make_password_change(inputData);
        });

        verify(mockUserDAO).get(username);
        verify(mockCommonUserFactory).create(username, newPassword);
        verify(mockUserDAO).update(username, mockNewCommonUser);
        verify(mockPresenter, never()).presentSuccess();
    }

    @Test
    @DisplayName("Should handle user factory returning null")
    void shouldHandleUserFactoryReturningNull() {
        // Given
        String username = "testUser";
        String newPassword = "newPassword";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, newPassword, null);

        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockCommonUserFactory.create(username, newPassword)).thenReturn(null);

        // When
        interactor.make_password_change(inputData);

        // Then
        verify(mockUserDAO).get(username);
        verify(mockCommonUserFactory).create(username, newPassword);
        verify(mockUserDAO).update(username, null);
        verify(mockPresenter).presentSuccess();
    }

    @Test
    @DisplayName("Should verify exact method call sequence for common user")
    void shouldVerifyExactMethodCallSequenceForCommonUser() {
        // Given
        String username = "testUser";
        String newPassword = "newPassword";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, newPassword, null);

        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockCommonUserFactory.create(username, newPassword)).thenReturn(mockNewCommonUser);

        // When
        interactor.make_password_change(inputData);

        // Then - verify exact call sequence
        verify(mockUserDAO, times(1)).get(username);
        verify(mockCommonUserFactory, times(1)).create(username, newPassword);
        verify(mockUserDAO, times(1)).update(username, mockNewCommonUser);
        verify(mockPresenter, times(1)).presentSuccess();

        verifyNoMoreInteractions(mockUserDAO);
        verifyNoMoreInteractions(mockCommonUserFactory);
        verifyNoMoreInteractions(mockSecurityUserFactory);
        verifyNoMoreInteractions(mockPresenter);
    }

    @Test
    @DisplayName("Should verify exact method call sequence for security user")
    void shouldVerifyExactMethodCallSequenceForSecurityUser() {
        // Given
        String username = "securityUser";
        String newPassword = "newPassword";
        String securityAnswer = "answer";
        String securityQuestion = "question";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, newPassword, securityAnswer);

        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockUserDAO.getAnswer(username)).thenReturn(securityAnswer);
        when(mockUserDAO.getQuestion(username)).thenReturn(securityQuestion);
        when(mockSecurityUserFactory.create(username, newPassword, securityQuestion, securityAnswer)).thenReturn(mockNewSecurityUser);

        // When
        interactor.make_password_change(inputData);

        // Then - verify exact call sequence
        verify(mockUserDAO, times(1)).get(username);
        verify(mockUserDAO, times(1)).getAnswer(username);
        verify(mockUserDAO, times(1)).getQuestion(username);
        verify(mockSecurityUserFactory, times(1)).create(username, newPassword, securityQuestion, securityAnswer);
        verify(mockUserDAO, times(1)).update(username, mockNewSecurityUser);
        verify(mockPresenter, times(1)).presentSuccess();

        verifyNoMoreInteractions(mockUserDAO);
        verifyNoMoreInteractions(mockCommonUserFactory);
        verifyNoMoreInteractions(mockSecurityUserFactory);
        verifyNoMoreInteractions(mockPresenter);
    }
}