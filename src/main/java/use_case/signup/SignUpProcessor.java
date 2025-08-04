package use_case.signup;

import java.util.regex.Pattern;

public class SignUpProcessor {

    private final SignupUserDataAccessInterface signupUserDataAccess;
    private final Pattern passwordRule;

    public SignUpProcessor(SignupUserDataAccessInterface signupUserDataAccess, Pattern passwordRule) {
        this.signupUserDataAccess = signupUserDataAccess;
        this.passwordRule = passwordRule;
    }

    /**
     * Processes a user signup request by validating username uniqueness and password criteria.
     *
     * @param username the username to register
     * @param pwd1     the password entered by the user
     * @param pwd2     the password confirmation entered by the user
     * @return a ProcessorOutput indicating success or failure and an error message if applicable
     */
    public ProcessorOutput signUpProcessor(String username, String pwd1, String pwd2) {
        ProcessorOutput output = null;
        if (signupUserDataAccess.existsByName(username)) {
            output = new ProcessorOutput(false, "User already exists.");
        }

        else if (!pwd1.equals(pwd2)) {
            output = new ProcessorOutput(false, "Passwords don't match.");
        }

        else if (!passwordRule.matcher(pwd1).matches()) {
            output = new ProcessorOutput(
                    false, "Password must be at least 6 characters and contain both letters and numbers.");
        }
        else {
            output = new ProcessorOutput(true, null);
        }
        return output;
    }

}
