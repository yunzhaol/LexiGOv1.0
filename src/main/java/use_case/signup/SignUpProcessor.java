package use_case.signup;

import java.util.ArrayList;
import java.util.List;
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
 * <p>Example usage:</p>
 * <pre>{@code
 * Pattern passwordPattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d).{8,}$");
 * SignUpProcessor processor = new SignUpProcessor(userDao, passwordPattern);
 * ProcessorOutput result = processor.signUpProcessor("john_doe", "Secret123", "Secret123");
 * }</pre>
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
        // Build the chain: username uniqueness → password match → password strength
        this.chain = builder()
                .addUserExistsCheck(dao)
                .addPasswordCheck()
                .addPasswordStrengthCheck(passwordRule)
                .build();
    }

    /**
     * Returns a new builder for constructing a custom chain of processors.
     *
     * @return a {@link Builder} instance
     */
    public static Builder builder() {
        return new Builder();
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

    /**
     * A builder class for constructing a sequential chain of validation processors.
     * Each added processor is linked to the next, forming a chain of responsibility.
     */
    public static final class Builder {
        /** Stores processors in the order they should execute. */
        private List<Processor> processors = new ArrayList<>();

        /**
         * Adds a check to ensure that the username does not already exist.
         *
         * @param dao the data access interface to check username availability
         * @return this builder instance
         */
        public Builder addUserExistsCheck(SignupUserDataAccessInterface dao) {
            final UsernameNotExistsProcessor usernameNotExistsProcessor = new UsernameNotExistsProcessor(dao);
            processors.add(usernameNotExistsProcessor);
            return this;
        }

        /**
         * Adds a check to ensure that the two entered passwords match.
         *
         * @return this builder instance
         */
        public Builder addPasswordCheck() {
            final PasswordMatchProcessor passwordMatchProcessor = new PasswordMatchProcessor();
            processors.add(passwordMatchProcessor);
            return this;
        }

        /**
         * Adds a password strength validation based on a given regex rule.
         *
         * @param passwordRule the compiled regex pattern for password requirements
         * @return this builder instance
         */
        public Builder addPasswordStrengthCheck(Pattern passwordRule) {
            final PasswordStrengthProcessor passwordStrengthProcessor = new PasswordStrengthProcessor(passwordRule);
            processors.add(passwordStrengthProcessor);
            return this;
        }

        /**
         * Adds any custom validation processor to the chain.
         *
         * @param otherProcessor the custom processor to add
         * @return this builder instance
         */
        public Builder addOtherCheck(Processor otherProcessor) {
            processors.add(otherProcessor);
            return this;
        }

        /**
         * Builds the processor chain, linking each processor to the next.
         *
         * @return the head processor of the chain
         * @throws IllegalStateException if no processors were added
         */
        public Processor build() {
            if (processors.isEmpty()) {
                throw new IllegalStateException("No processors configured.");
            }
            final Processor head = processors.get(0);
            Processor prev = head;
            for (int i = 1; i < processors.size(); i++) {
                final Processor cur = processors.get(i);
                prev.setNext(cur);
                prev = cur;
            }
            return head;
        }
    }
}
