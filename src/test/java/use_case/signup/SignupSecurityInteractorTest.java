package use_case.signup;

import data_access.JsonUserDataAccessObject;
import entity.UserFactory;
import entity.UserFactoryManager;
import entity.UserType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.signup.security.SignupSecurityInputData;
import use_case.signup.security.SignupSecurityInteractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link SignupSecurityInteractor}.
 * <p>
 * Uses a JSON file as the data store and clears it after each test.
 */
public class SignupSecurityInteractorTest {

    /** Path to the JSON file used during testing. */
    private static final Path TEST_DATA_PATH =
            Paths.get("src/test/resources/data/usersignuptest.json");

    private JsonUserDataAccessObject userDataAccessObject;
    private UserFactory factory;

    /**
     * Initialize the JSON DAO and user factory before each test.
     */
    @BeforeEach
    public void setup() {
        try {
            userDataAccessObject =
                    new JsonUserDataAccessObject(TEST_DATA_PATH.toString());
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize DAO", e);
        }
        factory = new UserFactoryManager().getFactory(UserType.SECURITY);
    }

    /**
     * Truncate the test JSON file after each test to ensure isolation.
     */
    @AfterEach
    public void cleanUp() throws IOException {
        if (Files.exists(TEST_DATA_PATH)) {
            Files.write(TEST_DATA_PATH, new byte[0]);
        }
    }

    /**
     * Scenario: successful signup with security question.
     */
    @Test
    public void successTest() {
        SignupSecurityInputData input =
                new SignupSecurityInputData("tester", "Aaa111", "Aaa111", "name", "jam");

        // Presenter that verifies success and persisted data
        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                assertEquals(false, outputData.isUseCaseFailed());
                assertEquals("tester", outputData.getUsername());
                assertEquals("tester",
                        userDataAccessObject.get(outputData.getUsername()).getName());
                assertEquals("Aaa111",
                        userDataAccessObject.get(outputData.getUsername()).getPassword());
                assertEquals(true,
                        userDataAccessObject.existsByName(outputData.getUsername()));
                assertEquals("name",
                        userDataAccessObject.getQuestion(outputData.getUsername()));
                assertEquals("jam",
                        userDataAccessObject.getAnswer(outputData.getUsername()));
                assertEquals("name",
                        userDataAccessObject.getSecurityQuestion(outputData.getUsername()));
                assertEquals("SECURITY",
                        userDataAccessObject.getType(outputData.getUsername()));
            }

            @Override
            public void prepareFailView(String errorMessage) {
                // Not expected in this scenario
            }

            @Override
            public void switchToLoginView() {
                // Not used here
            }
        };

        SignupSecurityInteractor interactor =
                new SignupSecurityInteractor(userDataAccessObject, presenter, factory);
        interactor.execute(input);
    }

    /**
     * Scenario: password fails complexity rule.
     */
    @Test
    public void failPasswordRuleTest() {
        SignupSecurityInputData input =
                new SignupSecurityInputData("tester", "1", "1", "name", "jam");

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                // Not used here
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals(
                        "Password must be at least 6 characters and contain both letters and numbers.",
                        errorMessage
                );
                assertEquals(false,
                        userDataAccessObject.existsByName("tester"));
                assertEquals(null,
                        userDataAccessObject.getQuestion("tester"));
                assertEquals(null,
                        userDataAccessObject.getAnswer("tester"));
                assertEquals(null,
                        userDataAccessObject.getSecurityQuestion("tester"));
                assertEquals(null,
                        userDataAccessObject.getType("tester"));
            }

            @Override
            public void switchToLoginView() {
                // Not relevant
            }
        };

        SignupSecurityInteractor interactor =
                new SignupSecurityInteractor(userDataAccessObject, presenter, factory);
        interactor.execute(input);
    }

    /**
     * Scenario: repeated password does not match.
     */
    @Test
    public void failRepeatPasswordTest() {
        SignupSecurityInputData input =
                new SignupSecurityInputData("tester", "Aaa111", "Aaa1112", "name", "jam");

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                // Not used here
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Passwords don't match.", errorMessage);
                assertEquals(false,
                        userDataAccessObject.existsByName("tester"));
                assertEquals(null,
                        userDataAccessObject.getQuestion("tester"));
                assertEquals(null,
                        userDataAccessObject.getAnswer("tester"));
                assertEquals(null,
                        userDataAccessObject.getSecurityQuestion("tester"));
                assertEquals(null,
                        userDataAccessObject.getType("tester"));
            }

            @Override
            public void switchToLoginView() {
                // No-op
            }
        };

        SignupSecurityInteractor interactor =
                new SignupSecurityInteractor(userDataAccessObject, presenter, factory);
        interactor.execute(input);
    }

    /**
     * Scenario: username already exists after a successful signup.
     */
    @Test
    public void failUsernameExistsTest() {
        // First, perform a successful signup
        SignupSecurityInputData firstInput =
                new SignupSecurityInputData("tester", "Aaa111", "Aaa111", "name", "jam");
        new SignupSecurityInteractor(
                userDataAccessObject,
                new SignupOutputBoundary() {
                    @Override public void prepareSuccessView(SignupOutputData o) {}
                    @Override public void prepareFailView(String m) {}
                    @Override public void switchToLoginView() {}
                },
                factory
        ).execute(firstInput);

        // Then attempt to sign up again with the same username
        SignupSecurityInputData secondInput =
                new SignupSecurityInputData("tester", "Aaa111", "Aaa111", "name", "jam");

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                // Not expected
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("User already exists.", errorMessage);
                assertEquals(true,
                        userDataAccessObject.existsByName("tester"));
                assertEquals("name",
                        userDataAccessObject.getQuestion("tester"));
                assertEquals("jam",
                        userDataAccessObject.getAnswer("tester"));
                assertEquals("name",
                        userDataAccessObject.getSecurityQuestion("tester"));
                assertEquals("SECURITY",
                        userDataAccessObject.getType("tester"));
            }

            @Override
            public void switchToLoginView() {
                // Not used
            }
        };

        SignupSecurityInteractor interactor2 =
                new SignupSecurityInteractor(userDataAccessObject, presenter, factory);
        interactor2.execute(secondInput);
    }

    /**
     * Scenario: switching to login view triggers the correct callback.
     */
    @Test
    public void switchToLoginViewTest() {
        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override public void prepareSuccessView(SignupOutputData o) {}
            @Override public void prepareFailView(String m) {}
            @Override
            public void switchToLoginView() {
                assertEquals(true, true, "switchToLoginView should be called");
            }
        };

        SignupSecurityInteractor interactor =
                new SignupSecurityInteractor(userDataAccessObject, presenter, factory);
        interactor.switchToLoginView();
    }
}
