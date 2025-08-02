package use_case.logout;


import data_access.JsonUserDataAccessObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogoutInteractorTest {

    private JsonUserDataAccessObject userDataAccessObject;
    @BeforeEach
    public void setup() {
        try {
            userDataAccessObject = new JsonUserDataAccessObject("src/test/resources/data/users.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void SuccessTest() {

        LogoutInputData input = new LogoutInputData("test01");

        LogoutOutputBoundary presenter = new LogoutOutputBoundary() {
            @Override
            public void prepareSuccessView(LogoutOutputData outputData) {
                assertEquals(outputData.getUsername(), "test01");
                assertEquals(outputData.isUseCaseFailed(), false);
                assertEquals(userDataAccessObject.getCurrentUsername(), null);
            }

            @Override
            public void prepareFailView(String errorMessage) {

            }
        };
        LogoutInteractor interactor = new LogoutInteractor(userDataAccessObject, presenter);
        interactor.execute(input);

    }
}