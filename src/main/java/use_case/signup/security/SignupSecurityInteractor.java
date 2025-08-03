package use_case.signup.security;

import entity.SecurityUserFactory;
import entity.User;
import entity.UserFactory;
import entity.dto.SecurityUserDto;
import use_case.signup.*;

import java.util.regex.Pattern;

/**
 * The Signup Interactor.
 */
public class SignupSecurityInteractor implements SignupSecurityInputBoundary {
    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory<SecurityUserDto> userFactory;
    private final SignUpProcessor processor;
    private static final Pattern PASSWORD_RULE =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{6,}$");

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

        String username = signupInputData.getUsername();
        String password = signupInputData.getPassword();
        String repeatPassword = signupInputData.getRepeatPassword();

        ProcessorOutput output = processor.signUpProcessor(username, password, repeatPassword);

        if (!output.isSuccess()) {
            userPresenter.prepareFailView(output.getErrorMessage());
        } else {
            SecurityUserDto dto = SecurityUserDto.builder()
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
