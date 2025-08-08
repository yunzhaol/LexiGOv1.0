package use_case.signup;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import use_case.signup.validation.*;

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
        // for extension, switch the order or adding more processor here
        Processor chainHead =
                builder()
                        .addUserExistsCheck(dao)
                        .addPasswordCheck()
                        .addPasswordStrengthCheck(passwordRule)
                        // .addYourCheck()
                        .build();
        this.chain = chainHead;
    }

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

    // inner class
    private static class Builder {
        private List<Processor> processors =  new ArrayList<>();

        public Builder addUserExistsCheck(SignupUserDataAccessInterface dao) {
            final UsernameNotExistsProcessor usernameNotExistsProcessor = new UsernameNotExistsProcessor(dao);
            processors.add(usernameNotExistsProcessor);
            return this;
        }

        public Builder addPasswordCheck() {
            final PasswordMatchProcessor passwordMatchProcessor = new PasswordMatchProcessor();
            processors.add(passwordMatchProcessor);
            return this;
        }

        public Builder addPasswordStrengthCheck(Pattern passwordRule) {
            final PasswordStrengthProcessor passwordStrengthProcessor = new PasswordStrengthProcessor(passwordRule);
            processors.add(passwordStrengthProcessor);
            return this;
        }

        public Builder addOtherCheck(Processor otherProcessor) {
            processors.add(otherProcessor);
            return this;
        }
        // for extension add
        // public Builder addYourCheck() {.....return this;}

        public Processor build() {
            if (processors.isEmpty()) {
                throw new IllegalStateException("No processors configured.");
            }
            Processor head = processors.get(0);
            Processor prev = head;
            for (int i = 1; i < processors.size(); i++) {
                Processor cur = processors.get(i);
                prev.setNext(cur);
                prev = cur;
            }
            return head;
        }
    }
}
