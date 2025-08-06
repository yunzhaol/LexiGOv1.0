package use_case.signup.validation;

/**
 * A processor in the signup validation chain that checks
 * whether the two entered passwords match.
 *
 * <p>If the passwords do not match, it returns a failure {@link ProcessorOutput}.
 * Otherwise, it passes control to the next processor in the chain if one exists.</p>
 */
public class PasswordMatchProcessor implements Processor {

    // Reference to the next processor in the chain
    private Processor next;

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
     * Validates that the two provided passwords match.
     * If not, returns a failure result. Otherwise, delegates to the next processor.
     *
     * @param username the username input (unused here)
     * @param pwd1     the primary password input
     * @param pwd2     the repeated password input
     * @return a {@link ProcessorOutput} indicating success or failure
     */
    @Override
    public ProcessorOutput process(String username, String pwd1, String pwd2) {
        ProcessorOutput output = null;

        // If the two passwords do not match, return a failure result
        if (!pwd1.equals(pwd2)) {
            output = ProcessorOutput.fail("Passwords don't match.");
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
