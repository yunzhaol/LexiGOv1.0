package use_case.achievement;

import data_access.JsonUserRecordDataAccessObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AchievementInteractorTest {

    private JsonUserRecordDataAccessObject mockDataAccess;

    @BeforeEach
    void setUp() {
        mockDataAccess = new JsonUserRecordDataAccessObject(
                "src/test/resources/testAchievement.json");
    }

    @Test
    void testNoAchievementsUnlocked() {

        AchievementOutputBoundary presenter = new AchievementOutputBoundary() {
            @Override
            public void present(AchievementOutputData responseModel) {
                assertNotNull(responseModel);
                assertEquals(responseModel.getUnlockedAchievements().size(), 6);
            }
        };

        AchievementInteractor interactor = new AchievementInteractor(presenter, mockDataAccess);

        AchievementInputData input = new AchievementInputData("AchievementTester");

        interactor.evaluate(input);

    }

    @Test
    void testOnlyFirstWordAchievementUnlocked() {
        mockDataAccess = new JsonUserRecordDataAccessObject(
                "src/test/resources/AchievementFirsttest.json");
        AchievementOutputBoundary presenter = new AchievementOutputBoundary() {
            @Override
            public void present(AchievementOutputData responseModel) {
                assertNotNull(responseModel);
                assertEquals(responseModel.getUnlockedAchievements().size(), 2);
                assertEquals(responseModel.getUnlockedAchievements().get(0), "You have learned 1 time");
                assertEquals(responseModel.getUnlockedAchievements().get(1), "First Word I Learned");
            }
        };

        AchievementInteractor interactor = new AchievementInteractor(presenter, mockDataAccess);

        AchievementInputData input = new AchievementInputData("AchievementTester");

        interactor.evaluate(input);
    }
}