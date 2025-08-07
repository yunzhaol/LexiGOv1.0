package use_case.signup.validation;

/**
 * Represents the result of a validation step in the signup processor chain.
 *
 * <p>This class is used to communicate whether a specific {@link Processor}
 * has successfully validated its input, along with an optional error message
 * in the case of failure.</p>
 *
 * <p>Instances of this class are immutable and should be created via the static
 * factory methods {@link #ok()} and {@link #fail(String)}.</p>
 */
public final class ProcessorOutput {

    /** The error message associated with the failure, or {@code null} if successful. */
    private final String errorMessage;

    /** Indicates whether the validation step was successful. */
    private final boolean success;

    /**
     * Constructs a new {@code ProcessorOutput}.
     *
     * @param success whether the validation passed
     * @param errorMessage the error message, or {@code null} if no error
     */
    public ProcessorOutput(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    /**
     * Returns the error message, if any.
     *
     * @return the error message, or {@code null} if the validation was successful
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Indicates whether the validation step succeeded.
     *
     * @return {@code true} if validation succeeded; {@code false} otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Creates a successful {@code ProcessorOutput} instance.
     *
     * @return a success result with no error message
     */
    public static ProcessorOutput ok() {
        return new ProcessorOutput(true, null);
    }

    /**
     * Creates a failed {@code ProcessorOutput} instance with an error message.
     *
     * @param msg the error message describing the validation failure
     * @return a failure result containing the specified message
     */
    public static ProcessorOutput fail(String msg) {
        return new ProcessorOutput(false, msg);
    }
}
