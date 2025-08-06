package use_case.signup.validation;

import java.util.regex.Pattern;

/**
 * A processor in the signup validation chain that verifies
 * whether the provided password satisfies the required strength rule.
 *
 * <p>This class uses a {@link Pattern} to enforce password criteria,
 * such as minimum length and character composition.</p>
 *
 * <p>If the password does not match the rule, it returns a failure result.
 * Otherwise, it passes validation to the next processor in the chain
 * if one exists.</p>
 */
public class PasswordStrengthProcessor implements Processor {

    // Regex pattern used to validate password strength
    private final Pattern passwordRule;

    // Reference to the next processor in the chain
    private Processor next;

    /**
     * Constructs a PasswordStrengthProcessor with the given password pattern.
     *
     * @param passwordRule the regex pattern that defines password requirements
     */
    public PasswordStrengthProcessor(Pattern passwordRule) {
        this.passwordRule = passwordRule;
    }

    /**
     * Sets the next processor in the validation chain.
     *
     * @param next the next processor to be invoked if this validation passes
     */
    @Override
    public void setNext(Processor next) {
        this.next = next;
    }

    /**
     * Validates that the password matches the defined strength pattern.
     * If not, returns a failure output; otherwise delegates to the next processor if present.
     *
     * @param username the username to validate (unused here)
     * @param pwd1     the password to validate
     * @param pwd2     the repeated password (unused here)
     * @return a {@link ProcessorOutput} indicating success or failure
     */
    @Override
    public ProcessorOutput process(String username, String pwd1, String pwd2) {
        ProcessorOutput output = null;

        // If password does not match the required pattern, return failure
        if (!passwordRule.matcher(pwd1).matches()) {
            output = ProcessorOutput.fail("Password must be at least 6 characters and contain both letters and numbers.");
        }
        // If validation passes, delegate to the next processor if one exists
        else if (next != null) {
            output = next.process(username, pwd1, pwd2);
        }
        // If this is the last processor in the chain, return success
        else {
            output = ProcessorOutput.ok();
        }

        return output;
    }
}
