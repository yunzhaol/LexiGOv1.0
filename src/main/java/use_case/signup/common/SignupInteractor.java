package use_case.signup.common;

import entity.User;
import entity.UserFactory;
import use_case.signup.*;
import use_case.signup.security.SignupSecurityInputBoundary;
import use_case.signup.security.SignupSecurityInputData;

import java.util.regex.Pattern;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {

    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;
    private final SignUpProcessor processor;
    private static final Pattern PASSWORD_RULE =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{6,}$");

    public SignupInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
                            SignupOutputBoundary signupOutputBoundary,
                            UserFactory userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
        this.processor = new SignUpProcessor(userDataAccessObject, PASSWORD_RULE);
    }

    @Override
    public void execute(SignupInputData signupInputData) {

        String username = signupInputData.getUsername();
        String pwd1     = signupInputData.getPassword();
        String pwd2     = signupInputData.getRepeatPassword();

        ProcessorOutput out = processor.signUpProcessor(username, pwd1, pwd2);

        if (out.isSuccess() == false) {
            userPresenter.prepareFailView(out.getErrorMessage());
        } else {

            User user = userFactory.create(username, pwd1);
            userDataAccessObject.save(user);
            userPresenter.prepareSuccessView(new SignupOutputData(username, false));
        }
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}