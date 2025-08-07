package use_case.signup.security;

import java.util.regex.Pattern;

import entity.User;
import entity.UserFactory;
import entity.dto.SecurityUserDto;
import use_case.signup.SignUpProcessor;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.signup.validation.ProcessorOutput;

/**
 * The Signup Interactor.
 */
public class SignupSecurityInteractor implements SignupSecurityInputBoundary {
    private static final Pattern PASSWORD_RULE =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{6,}$");
    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory<SecurityUserDto> userFactory;
    private final SignUpProcessor processor;

    public SignupSecurityInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
                                    SignupOutputBoundary signupOutputBoundary,
                                    UserFactory<SecurityUserDto> userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
        this.processor = new SignUpProcessor(userDataAccessObject, PASSWORD_RULE);
    }

    @Override
    public void execute(SignupSecurityInputData signupInputData) {

        final String username = signupInputData.getUsername();
        final String password = signupInputData.getPassword();
        final String repeatPassword = signupInputData.getRepeatPassword();

        final ProcessorOutput output = processor.signUpProcessor(username, password, repeatPassword);

        if (!output.isSuccess()) {
            userPresenter.prepareFailView(output.getErrorMessage());
        }
        else {
            final SecurityUserDto dto = SecurityUserDto.builder()
                    .name(username)
                    .password(password)
                    .question(signupInputData.getSecurityQuestion())
                    .answer(signupInputData.getSecurityAnswer())
                    .build();

            final User user = userFactory.create(dto);
            userDataAccessObject.save(user);
            userPresenter.prepareSuccessView(new SignupOutputData(username, false));
        }
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}
