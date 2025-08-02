package use_case.signup;

import java.util.regex.Pattern;

public class SignUpProcessor {

    private final SignupUserDataAccessInterface signupUserDataAccess;
    private final Pattern PASSWORD_RULE;

    public SignUpProcessor(SignupUserDataAccessInterface signupUserDataAccess, Pattern passwordRule) {
        this.signupUserDataAccess = signupUserDataAccess;
        PASSWORD_RULE = passwordRule;
    }

    public ProcessorOutput signUpProcessor(String username, String pwd1, String pwd2) {
        if (signupUserDataAccess.existsByName(username)) {
            ProcessorOutput out = new ProcessorOutput(false, "User already exists.");
            return out;
        }

        if (!pwd1.equals(pwd2)) {
            ProcessorOutput out = new ProcessorOutput(false, "Passwords don't match.");
            return out;
        }

        if (!PASSWORD_RULE.matcher(pwd1).matches()) {
            ProcessorOutput out = new ProcessorOutput(
                    false, "Password must be at least 6 characters and contain both letters and numbers.");
            return out;
        }
        return new ProcessorOutput(true, null);
    }

}
