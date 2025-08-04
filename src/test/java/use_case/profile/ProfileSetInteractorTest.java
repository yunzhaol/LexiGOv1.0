package use_case.profile;

import entity.Language;
import entity.PersonalProfile;
import entity.PersonalProfileFactory;
import entity.ProfileFactory;
import org.junit.jupiter.api.Test;
import use_case.profile.profile_set.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProfileSetInteractorTest {

    @Test
    void testExecute_OldLanguageIsNull_Success() {
        ProfileSetInputData inputData = new ProfileSetInputData("testUser", null, Language.EN);

        ProfileSetUserDataAccessInterface mockDAO = mock(ProfileSetUserDataAccessInterface.class);
        ProfileSetOutputBoundary mockPresenter = new ProfileSetOutputBoundary() {
            @Override
            public void prepareSuccessView(ProfileSetOutputData outputData) {
                assertEquals("testUser", outputData.getUsername());
                assertEquals(Language.EN, outputData.getNewlanguage());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Should not call fail view");
            }
        };
//        ProfileFactory profileFactory = new ProfileFactory();
//
//        ProfileSetInteractor interactor = new ProfileSetInteractor(mockDAO, mockPresenter, profileFactory);
//        interactor.execute(inputData);
    }

    @Test
    void testExecute_OldLanguageEqualsNew_Fail() {
        ProfileSetInputData inputData = new ProfileSetInputData("testUser", Language.EN, Language.EN);

        ProfileSetUserDataAccessInterface mockDAO = mock(ProfileSetUserDataAccessInterface.class);
        ProfileSetOutputBoundary mockPresenter = new ProfileSetOutputBoundary() {
            @Override
            public void prepareSuccessView(ProfileSetOutputData outputData) {
                fail("Should not call success view");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("New language must be different from the old language.", error);
            }
        };
        ProfileFactory profileFactory = new PersonalProfileFactory();

        ProfileSetInteractor interactor = new ProfileSetInteractor(mockDAO, mockPresenter, profileFactory);
        interactor.execute(inputData);
    }

    @Test
    void testExecute_OldLanguageDifferentFromNew_Success() {
        ProfileSetInputData inputData = new ProfileSetInputData("testUser", Language.EN, Language.ZH);

        ProfileSetUserDataAccessInterface mockDAO = mock(ProfileSetUserDataAccessInterface.class);
        ProfileSetOutputBoundary mockPresenter = new ProfileSetOutputBoundary() {
            @Override
            public void prepareSuccessView(ProfileSetOutputData outputData) {
                assertEquals("testUser", outputData.getUsername());
                assertEquals(Language.ZH, outputData.getNewlanguage());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Should not call fail view");
            }
        };
        ProfileFactory profileFactory = new PersonalProfileFactory();

        ProfileSetInteractor interactor = new ProfileSetInteractor(mockDAO, mockPresenter, profileFactory);
        interactor.execute(inputData);
    }
}
