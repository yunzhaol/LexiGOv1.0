package use_case.profile;

import entity.Language;
import entity.PersonalProfileFactory;
import entity.ProfileFactory;
import org.junit.jupiter.api.Test;
import use_case.profile.profile_set.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link ProfileSetInteractor}.
 */
class ProfileSetInteractorTest {

    private final ProfileFactory profileFactory = new PersonalProfileFactory();

    @Test
    void execute_oldLanguageIsNull_success() {
        ProfileSetInputData inputData =
                new ProfileSetInputData("testUser", null, Language.EN);

        ProfileSetUserDataAccessInterface mockDao =
                mock(ProfileSetUserDataAccessInterface.class);

        ProfileSetOutputBoundary presenter = new ProfileSetOutputBoundary() {
            @Override
            public void prepareSuccessView(ProfileSetOutputData outputData) {
                assertEquals("testUser", outputData.getUsername());
                assertEquals(Language.EN, outputData.getNewlanguage());
            }

            @Override
            public void prepareFailView(String error) {
                fail("prepareFailView should not be called");
            }
        };

        ProfileSetInteractor interactor =
                new ProfileSetInteractor(mockDao, presenter, profileFactory);

        interactor.execute(inputData);
    }

    @Test
    void execute_oldLanguageEqualsNew_fail() {
        ProfileSetInputData inputData =
                new ProfileSetInputData("testUser", Language.EN, Language.EN);

        ProfileSetUserDataAccessInterface mockDao =
                mock(ProfileSetUserDataAccessInterface.class);

        ProfileSetOutputBoundary presenter = new ProfileSetOutputBoundary() {
            @Override
            public void prepareSuccessView(ProfileSetOutputData outputData) {
                fail("prepareSuccessView should not be called");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals(
                        "New language must be different from the old language.", error);
            }
        };

        ProfileSetInteractor interactor =
                new ProfileSetInteractor(mockDao, presenter, profileFactory);

        interactor.execute(inputData);
    }

    @Test
    void execute_oldLanguageDifferentFromNew_success() {
        ProfileSetInputData inputData =
                new ProfileSetInputData("testUser", Language.EN, Language.ZH);

        ProfileSetUserDataAccessInterface mockDao =
                mock(ProfileSetUserDataAccessInterface.class);

        ProfileSetOutputBoundary presenter = new ProfileSetOutputBoundary() {
            @Override
            public void prepareSuccessView(ProfileSetOutputData outputData) {
                assertEquals("testUser", outputData.getUsername());
                assertEquals(Language.ZH, outputData.getNewlanguage());
            }

            @Override
            public void prepareFailView(String error) {
                fail("prepareFailView should not be called");
            }
        };

        ProfileSetInteractor interactor =
                new ProfileSetInteractor(mockDao, presenter, profileFactory);

        interactor.execute(inputData);
    }
}