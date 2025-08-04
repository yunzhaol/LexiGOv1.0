package use_case.profile;

import data_access.JsonUserProfileDAO;
import data_access.JsonUserRecordDataAccessObject;
import entity.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import use_case.profile.*;
import use_case.profile.profile_set.ProfileSetOutputBoundary;
import use_case.rank.*;

public class ProfileInteractorTest {
    @Test
    void successTest() {
        ProfileInputData inputData = new ProfileInputData("test01");
        //JsonUserRecordDataAccessObject dao = new JsonUserRecordDataAccessObject("src/test/resources/data/rankTest.json");
        JsonUserProfileDAO dao = new JsonUserProfileDAO("src/test/resources/data/");
        // This creates a successPresenter that tests whether the test case is as we expect.
        ProfileOutputBoundary successPresenter = new ProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(ProfileOutputData outputData) {
                assertEquals("test01", outputData.getUsername());
                assertEquals(Language.ZH, outputData.getLanguage());
                assertEquals(Language.EN, outputData.getLanguages()[0]);
            }
        };

        ProfileInteractor interactor = new ProfileInteractor(successPresenter, dao);
        interactor.execute(inputData);
    }
}
