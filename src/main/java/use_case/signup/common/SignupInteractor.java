package use_case.signup.common;

import java.util.regex.Pattern;

import entity.User;
import entity.UserFactory;
import entity.dto.CommonUserDto;
import use_case.signup.SignUpProcessor;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.signup.validation.ProcessorOutput;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {

    private static final Pattern PASSWORD_RULE =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{6,}$");
    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory<CommonUserDto> userFactory;
    private final SignUpProcessor processor;

    public SignupInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
                            SignupOutputBoundary signupOutputBoundary,
                            UserFactory<CommonUserDto> userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
        this.processor = new SignUpProcessor(userDataAccessObject, PASSWORD_RULE);
    }

    @Override
    public void execute(SignupInputData signupInputData) {

        final String username = signupInputData.getUsername();
        final String pwd1 = signupInputData.getPassword();
        final String pwd2 = signupInputData.getRepeatPassword();

        final ProcessorOutput out = processor.signUpProcessor(username, pwd1, pwd2);

        if (!out.isSuccess()) {
            userPresenter.prepareFailView(out.getErrorMessage());
        }
        else {
            final CommonUserDto dto = CommonUserDto.builder()
                    .name(username)
                    .password(pwd1)
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
