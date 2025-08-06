package use_case.change_password.make_password_change;

import entity.User;
import entity.UserFactory;
import entity.dto.CommonUserDto;
import entity.dto.SecurityUserDto;
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

        interactor = new MakePasswordChangeInteractor(mockPresenter, mockUserDAO, mockCommonUserFactory,
                mockSecurityUserFactory);
    }

    @Test
    @DisplayName("Should successfully change password for common user")
    void shouldSuccessfullyChangePasswordForCommonUser() {
        // Given
        String username = "commonUser";
        String newPassword = "newPassword123";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, newPassword, null);

        when(mockUserDAO.get(username)).thenReturn(mockUser);
        CommonUserDto dto = new CommonUserDto(username, newPassword);
        when(mockCommonUserFactory.create(dto)).thenReturn(mockNewCommonUser);

        // When
        interactor.makePasswordChange(inputData);

        // Then
        verify(mockUserDAO).get(username);
        CommonUserDto dto10 = new CommonUserDto(username, newPassword);
        verify(mockCommonUserFactory).create(dto10);
        verify(mockUserDAO).update(username, mockNewCommonUser);
        verify(mockPresenter).presentSuccess();
        verify(mockPresenter, never()).presentFailure(any());
    }

    @Test
    @DisplayName("Should fail when common user password is empty string")
    void shouldFailWhenCommonUserPasswordIsEmptyString() {
        // Given
        String username = "commonUser";
        String emptyPassword = "";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(username, emptyPassword, null);

        when(mockUserDAO.get(username)).thenReturn(mockUser);

        // When
        interactor.makePasswordChange(inputData);

        // Then
        verify(mockUserDAO).get(username);
        verify(mockCommonUserFactory, never()).create(any());
        verify(mockUserDAO, never()).update(any(), any());
        verify(mockPresenter, never()).presentSuccess();

        // Capture failure
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
        SecurityUserDto dto = new SecurityUserDto(username, newPassword, securityQuestion, securityAnswer);
        when(mockSecurityUserFactory.create(dto)).thenReturn(mockNewSecurityUser);

        // When
        interactor.makePasswordChange(inputData);

        // Then
        verify(mockUserDAO).get(username);
        verify(mockUserDAO).getAnswer(username);
        verify(mockUserDAO).getQuestion(username);
        SecurityUserDto dto11 = new SecurityUserDto(username, newPassword, securityQuestion, securityAnswer);
        verify(mockSecurityUserFactory).create(dto11);
        verify(mockUserDAO).update(username, mockNewSecurityUser);
        verify(mockPresenter).presentSuccess();
        verify(mockPresenter, never()).presentFailure(any());
    }

    @Test
    @DisplayName("Should not proceed when security answer is incorrect")
    void shouldNotProceedWhenSecurityAnswerIsIncorrect() {
        // Given
        String username = "securityUser";
        String newPassword = "newPassword123";
        String correctAnswer = "correctAnswer";
        String wrongAnswer = "wrongAnswer";
        MakePasswordChangeInputData inputData =
                new MakePasswordChangeInputData(username, newPassword, wrongAnswer);

        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockUserDAO.getAnswer(username)).thenReturn(correctAnswer);

        // When
        interactor.makePasswordChange(inputData);

        // Then
        verify(mockUserDAO).get(username);
        verify(mockUserDAO).getAnswer(username);
        verify(mockUserDAO, never()).getQuestion(any());
        verify(mockSecurityUserFactory, never()).create(any());
        verify(mockUserDAO, never()).update(any(), any());
        verify(mockPresenter, never()).presentSuccess();

        verify(mockPresenter).presentFailure(argThat(output ->
                output != null &&
                        "Wrong answer".equals(output.getErrorMessage())
        ));

        verifyNoMoreInteractions(mockUserDAO, mockSecurityUserFactory, mockCommonUserFactory, mockPresenter);
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
            interactor.makePasswordChange(inputData);
        });

        verify(mockUserDAO).get(username);
        verify(mockUserDAO).getAnswer(username);
    }

    @Test
    @DisplayName("Should treat security answer as case-sensitive and fail on mismatch")
    void shouldHandleCaseSensitiveSecurityAnswerComparison() {
        // Given
        String username = "securityUser";
        String newPassword = "newPassword123";
        String correctAnswer = "Blue";             // stored answer
        String userInputAnswer = "blue";           // user-provided answer (lowercase)

        MakePasswordChangeInputData inputData =
                new MakePasswordChangeInputData(username, newPassword, userInputAnswer);

        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockUserDAO.getAnswer(username)).thenReturn(correctAnswer);

        // When
        interactor.makePasswordChange(inputData);

        // Then - Should detect answer mismatch (case-sensitive)
        verify(mockUserDAO).get(username);
        verify(mockUserDAO).getAnswer(username);

        verify(mockPresenter).presentFailure(argThat(output ->
                output != null &&
                        output.getErrorMessage() != null &&
                        output.getErrorMessage().equals("Wrong answer")
        ));

        verify(mockUserDAO, never()).getQuestion(any());
        verify(mockSecurityUserFactory, never()).create(any());
        verify(mockUserDAO, never()).update(any(), any());
        verify(mockPresenter, never()).presentSuccess();

        verifyNoMoreInteractions(mockUserDAO, mockSecurityUserFactory, mockCommonUserFactory, mockPresenter);
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
        SecurityUserDto dto__ = new SecurityUserDto(username, newPassword, securityQuestion, answerWithSpaces);
        when(mockSecurityUserFactory.create(dto__)).thenReturn(mockNewSecurityUser);

        // When
        interactor.makePasswordChange(inputData);

        // Then
        verify(mockUserDAO).get(username);
        verify(mockUserDAO).getAnswer(username);
        verify(mockUserDAO).getQuestion(username);
        SecurityUserDto dto___ = new SecurityUserDto(username, newPassword, securityQuestion, answerWithSpaces);
        verify(mockSecurityUserFactory).create(dto___);
        verify(mockUserDAO).update(username, mockNewSecurityUser);
        verify(mockPresenter).presentSuccess();
    }

    @Test
    @DisplayName("Should fail when new password equals the old password")
    void shouldRejectSamePassword() {
        // Given
        String username = "duplicateUser";
        String oldPassword = "password123";
        String newPassword = "password123";          // 与旧密码完全相同
        MakePasswordChangeInputData inputData =
                new MakePasswordChangeInputData(username, newPassword, null);  // 无密保问题

        // 模拟 DAO 行为
        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockUser.getPassword()).thenReturn(oldPassword);

        // When
        interactor.makePasswordChange(inputData);

        // Then
        // 1⃣ 捕获 presenter 传出的错误信息
        ArgumentCaptor<MakePasswordChangeOutputData> captor =
                ArgumentCaptor.forClass(MakePasswordChangeOutputData.class);
        verify(mockPresenter).presentFailure(captor.capture());
        assertEquals(
                "Password cannot be same as the old password",
                captor.getValue().getErrorMessage());

        // 2⃣ 确认没有执行更新与成功回调
        verify(mockUserDAO, never()).update(any(), any());
        verify(mockPresenter, never()).presentSuccess();
    }

    @Test
    @DisplayName("Should handle empty username")
    void shouldHandleEmptyUsername() {
        // Given
        String emptyUsername = "";
        MakePasswordChangeInputData inputData = new MakePasswordChangeInputData(emptyUsername, "password", null);

        CommonUserDto dto______ = new CommonUserDto(emptyUsername, "password");
        when(mockUserDAO.get(emptyUsername)).thenReturn(mockUser);
        when(mockCommonUserFactory.create(dto______)).thenReturn(mockNewCommonUser);

        // When
        interactor.makePasswordChange(inputData);

        // Then
        CommonUserDto dto_______ = new CommonUserDto(emptyUsername, "password");
        verify(mockUserDAO).get(emptyUsername);
        verify(mockCommonUserFactory).create(dto_______);
        verify(mockUserDAO).update(emptyUsername, mockNewCommonUser);
        verify(mockPresenter).presentSuccess();
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
            interactor.makePasswordChange(inputData);
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
        CommonUserDto dto2 = new CommonUserDto(username, newPassword);
        when(mockCommonUserFactory.create(dto2)).thenReturn(mockNewCommonUser);
        doThrow(new RuntimeException("Update failed")).when(mockUserDAO).update(username, mockNewCommonUser);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            interactor.makePasswordChange(inputData);
        });

        CommonUserDto dto3 = new CommonUserDto(username, newPassword);
        verify(mockUserDAO).get(username);
        verify(mockCommonUserFactory).create(dto3);
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

        CommonUserDto dto4 = new CommonUserDto(username, newPassword);
        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockCommonUserFactory.create(dto4)).thenReturn(null);

        // When
        interactor.makePasswordChange(inputData);

        // Then
        CommonUserDto dto5 = new CommonUserDto(username, newPassword);
        verify(mockUserDAO).get(username);
        verify(mockCommonUserFactory).create(dto5);
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

        CommonUserDto dto6 = new CommonUserDto(username, newPassword);
        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockCommonUserFactory.create(dto6)).thenReturn(mockNewCommonUser);

        // When
        interactor.makePasswordChange(inputData);

        // Then - verify exact call sequence
        verify(mockUserDAO, times(1)).get(username);
        verify(mockCommonUserFactory, times(1)).create(dto6);
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

        SecurityUserDto dto8 = new SecurityUserDto(username, newPassword, securityQuestion, securityAnswer);
        when(mockUserDAO.get(username)).thenReturn(mockUser);
        when(mockUserDAO.getAnswer(username)).thenReturn(securityAnswer);
        when(mockUserDAO.getQuestion(username)).thenReturn(securityQuestion);
        when(mockSecurityUserFactory.create(dto8)).thenReturn(mockNewSecurityUser);

        // When
        interactor.makePasswordChange(inputData);

        // Then - verify exact call sequence
        SecurityUserDto dto9 = new SecurityUserDto(username, newPassword, securityQuestion, securityAnswer);
        verify(mockUserDAO, times(1)).get(username);
        verify(mockUserDAO, times(1)).getAnswer(username);
        verify(mockUserDAO, times(1)).getQuestion(username);
        verify(mockSecurityUserFactory).create(refEq(dto8));
        verify(mockUserDAO, times(1)).update(username, mockNewSecurityUser);
        verify(mockPresenter, times(1)).presentSuccess();

        verifyNoMoreInteractions(mockUserDAO);
        verifyNoMoreInteractions(mockCommonUserFactory);
        verifyNoMoreInteractions(mockSecurityUserFactory);
        verifyNoMoreInteractions(mockPresenter);
    }
}