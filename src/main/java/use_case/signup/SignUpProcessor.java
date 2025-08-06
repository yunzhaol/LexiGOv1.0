package use_case.signup;

import java.util.regex.Pattern;

import use_case.signup.validation.PasswordMatchProcessor;
import use_case.signup.validation.PasswordStrengthProcessor;
import use_case.signup.validation.Processor;
import use_case.signup.validation.ProcessorOutput;
import use_case.signup.validation.UsernameNotExistsProcessor;

/**
 * The {@code SignUpProcessor} class acts as the entry point to a chain of responsibility
 * that performs sequential validation checks during user signup.
 *
 * <p>It constructs a validation chain composed of multiple {@link Processor} instances,
 * each responsible for one specific rule (e.g., username uniqueness, password matching,
 * password strength). The chain is executed in order, and the first failing check
 * will short-circuit the process and return a failure result.</p>
 *
 * <p>This structure improves modularity, testability, and separation of concerns,
 * and supports the Open/Closed Principle for adding future validation rules.</p>
 *
 * @author [Your Name]
 */
public class SignUpProcessor {

    /** The head of the validation processor chain. */
    private final Processor chain;

    /**
     * Constructs the {@code SignUpProcessor} by setting up the validation chain.
     *
     * @param dao           the data access interface for checking existing usernames
     * @param passwordRule  the compiled regular expression pattern defining password rules
     */
    public SignUpProcessor(SignupUserDataAccessInterface dao, Pattern passwordRule) {
        // Create individual processors for each validation step
        final UsernameNotExistsProcessor usernameNotExistsProcessor = new UsernameNotExistsProcessor(dao);
        final PasswordMatchProcessor passwordMatchProcessor = new PasswordMatchProcessor();
        final PasswordStrengthProcessor passwordStrengthProcessor = new PasswordStrengthProcessor(passwordRule);

        // Chain them in order: username → password match → password strength
        usernameNotExistsProcessor.setNext(passwordMatchProcessor);
        passwordMatchProcessor.setNext(passwordStrengthProcessor);

        // Set chain head as the entry point of validation
        this.chain = usernameNotExistsProcessor;
    }

    /**
     * Executes the signup validation process by passing the credentials through
     * the validation chain.
     *
     * @param username the username input from the user
     * @param pwd1     the first password input
     * @param pwd2     the confirmation password input
     * @return a {@link ProcessorOutput} indicating success or failure and any error message
     */
    public ProcessorOutput signUpProcessor(String username, String pwd1, String pwd2) {
        return chain.process(username, pwd1, pwd2);
    }
}
