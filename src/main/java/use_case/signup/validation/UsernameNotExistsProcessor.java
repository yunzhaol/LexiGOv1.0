package use_case.signup.validation;

import use_case.signup.SignupUserDataAccessInterface;

/**
 * A processor in the signup validation chain that checks
 * whether the given username already exists in the system.
 *
 * <p>This class implements the Chain of Responsibility pattern:
 * if the username exists, it returns a failure output; otherwise,
 * it passes the validation to the next processor in the chain.</p>
 */
public class UsernameNotExistsProcessor implements Processor {

    // DAO interface used to check if a username already exists
    private final SignupUserDataAccessInterface signupUserDataAccess;

    // Reference to the next processor in the chain
    private Processor next;

    /**
     * Constructs a UsernameNotExistsProcessor with the given user DAO.
     *
     * @param dao the data access object used to check username existence
     */
    public UsernameNotExistsProcessor(SignupUserDataAccessInterface dao) {
        this.signupUserDataAccess = dao;
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
     * Processes the username validation.
     * If the username exists, returns a failure.
     * Otherwise, delegates to the next processor if present.
     *
     * @param username the username to validate
     * @param pwd1     the user's password (unused here)
     * @param pwd2     the repeated password (unused here)
     * @return a {@link ProcessorOutput} indicating success or failure
     */
    @Override
    public ProcessorOutput process(String username, String pwd1, String pwd2) {
        ProcessorOutput output = null;

        // If the username is already taken, return a failure result
        if (signupUserDataAccess.existsByName(username)) {
            output = ProcessorOutput.fail("User already exists.");
        }
        // Otherwise, pass to the next processor if it exists
        else if (next != null) {
            output = next.process(username, pwd1, pwd2);
        }
        // If this is the last processor, return success
        else {
            output = ProcessorOutput.ok();
        }

        return output;
    }
}
