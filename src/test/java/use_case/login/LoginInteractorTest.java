package use_case.login;

import data_access.JsonUserDataAccessObject;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LoginInteractorTest {
    @Test
    void successTest() throws IOException {
        LoginInputData inputData = new LoginInputData("test01", "Aaa111");
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject("src/test/resources/data/loginTest.json");

        // This creates a successPresenter that tests whether the test case is as we expect.
        LoginOutputBoundary successPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData outputData) {
                assertEquals("test01",  outputData.getUsername());
                assertEquals("test01", dao.getCurrentUsername());
            }

            @Override
            public void prepareFailView(String errorMessage) {

            }

            @Override
            public void switchToSignUpView() {

            }
        };

        LoginInteractor interactor = new LoginInteractor(dao, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureTestWrongPW() throws IOException {
        LoginInputData inputData = new LoginInputData("test01", "Aaa1");
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject("src/test/resources/data/loginTest.json");

        // This creates a failurePresenter that tests whether the test case is as we expect.
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData outputData) {
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Incorrect password for \"test01\".", errorMessage);
            }

            @Override
            public void switchToSignUpView() {

            }
        };

        LoginInteractor interactor = new LoginInteractor(dao, failurePresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureTestUserDNE() throws IOException {
        LoginInputData inputData = new LoginInputData("test3", "Aaa111");
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject("src/test/resources/data/loginTest.json");

        // This creates a failurePresenter that tests whether the test case is as we expect.
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData outputData) {
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("test3: Account does not exist.", errorMessage);
            }

            @Override
            public void switchToSignUpView() {

            }
        };

        LoginInteractor interactor = new LoginInteractor(dao, failurePresenter);
        interactor.execute(inputData);
    }

    @Test
    void switchTest() throws IOException {
        LoginInputData inputData = new LoginInputData("test01", "Aaa111");
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject("src/test/resources/data/loginTest.json");

        // This creates a successPresenter that tests whether the test case is as we expect.
        LoginOutputBoundary successPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData outputData) {
                assertEquals("test01",  outputData.getUsername());
            }

            @Override
            public void prepareFailView(String errorMessage) {

            }

            @Override
            public void switchToSignUpView() {
                assertFalse(false);
            }
        };

        LoginInteractor interactor = new LoginInteractor(dao, successPresenter);
        interactor.switchToSignUpView();
    }
}
