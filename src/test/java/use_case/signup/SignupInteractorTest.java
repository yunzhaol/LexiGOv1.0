package use_case.signup;

import data_access.JsonUserDataAccessObject;
import entity.DefaultUserFactory;
import entity.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SignupInteractorTest {

    private JsonUserDataAccessObject userDataAccessObject;
    private UserFactory factory;
    @BeforeEach
    public void setup() {
        try {
            userDataAccessObject = new JsonUserDataAccessObject("src/test/resources/data/usersignuptest.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        factory = new DefaultUserFactory();

    }

    @Test
    public void SuccessTest() {
        SignupInputData inRepeatPasswordFail = new SignupInputData("tester", "Aaa111", "1");
        SignupInputData inSuccess = new SignupInputData("tester", "Aaa111", "Aaa111");
        SignupInputData inUsernameExists = new SignupInputData("test01", "Aaa111", "Aaa111");

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                assertEquals(outputData.isUseCaseFailed(), false);
                assertEquals(outputData.getUsername(), "tester");
                assertEquals(userDataAccessObject.get(outputData.getUsername()).getName(), "tester");
                assertEquals(userDataAccessObject.get(outputData.getUsername()).getPassword(), "Aaa111");
                assertEquals(userDataAccessObject.existsByName(outputData.getUsername()), true);
                assertEquals(userDataAccessObject.getQuestion(outputData.getUsername()), null);
                assertEquals(userDataAccessObject.getAnswer(outputData.getUsername()), null);
                assertEquals(userDataAccessObject.getSecurityQuestion(outputData.getUsername()), null);
                assertEquals(userDataAccessObject.getType(outputData.getUsername()), "COMMON");
            }

            @Override
            public void prepareFailView(String errorMessage) {

            }

            @Override
            public void switchToLoginView() {

            }
        };
        SignupInteractor interactor = new SignupInteractor(userDataAccessObject, presenter, factory);
        interactor.execute(inSuccess);
    }

    @Test
    public void Fail1Test() {
        SignupInputData inPasswordRuleFail = new SignupInputData("tester", "1", "1");

    }
}