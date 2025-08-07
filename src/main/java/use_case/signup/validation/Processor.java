package use_case.signup.validation;

/**
 * Represents a single processing unit in a signup validation chain.
 *
 * <p>This interface follows the Chain of Responsibility design pattern,
 * where each {@code Processor} performs a specific validation task.
 * (e.g., checking username availability, password match, password strength).
 * After processing, it optionally delegates to the next processor in the chain.</p>
 *
 * <p>Each implementation should return a {@link ProcessorOutput} indicating
 * whether the validation succeeded or failed, and include an error message if applicable.</p>
 */
public interface Processor {

    /**
     * Sets the next processor in the chain.
     *
     * @param next the next {@code Processor} to call if the current validation passes
     */
    void setNext(Processor next);

    /**
     * Performs validation logic on the provided signup credentials.
     *
     * @param username the username to validate
     * @param pwd1     the primary password input
     * @param pwd2     the repeated password input for confirmation
     * @return a {@link ProcessorOutput} indicating success or failure of this validation step
     */
    ProcessorOutput process(String username, String pwd1, String pwd2);
}

