package use_case.signup;

import entity.User;
import entity.UserFactory;

import java.util.regex.Pattern;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {

    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;
    private static final Pattern PASSWORD_RULE =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{6,}$");

    public SignupInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
                            SignupOutputBoundary signupOutputBoundary,
                            UserFactory userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(SignupInputData signupInputData) {

        String username = signupInputData.getUsername();
        String pwd1     = signupInputData.getPassword();
        String pwd2     = signupInputData.getRepeatPassword();

        if (userDataAccessObject.existsByName(username)) {
            userPresenter.prepareFailView("User already exists.");
            return;
        }

        if (!pwd1.equals(pwd2)) {
            userPresenter.prepareFailView("Passwords don't match.");
            return;
        }

        if (!PASSWORD_RULE.matcher(pwd1).matches()) {
            userPresenter.prepareFailView(
                    "Password must be at least 6 characters and contain both letters and numbers.");
            return;
        }

        User user = userFactory.create(username, pwd1);
        userDataAccessObject.save(user);

        SignupOutputData out = new SignupOutputData(user.getName(), false);
        userPresenter.prepareSuccessView(out);
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}