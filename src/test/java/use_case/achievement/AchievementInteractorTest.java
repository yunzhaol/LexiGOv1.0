package use_case.achievement;

import data_access.JsonUserRecordDataAccessObject;
import entity.Achievement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AchievementInteractorTest {

    private JsonUserRecordDataAccessObject mockDataAccess;

    @BeforeEach
    void setUp() {
        mockDataAccess = new JsonUserRecordDataAccessObject(
                "src/test/resources/data/testAchievement.json");
    }

    @Test
    void testNoAchievementsUnlocked() {

        AchievementOutputBoundary presenter = new AchievementOutputBoundary() {
            @Override
            public void present(AchievementOutputData responseModel) {
                assertNotNull(responseModel);
                assertEquals(7, responseModel.getUnlockedAchievements().size());
            }
        };

        AchievementInteractor interactor = new AchievementInteractor(presenter, mockDataAccess);

        AchievementInputData input = new AchievementInputData("AchievementTester");

        interactor.evaluate(input);

    }

    @Test
    void testOnlyFirstWordAchievementUnlocked() {
        mockDataAccess = new JsonUserRecordDataAccessObject(
                "src/test/resources/data/AchievementFirsttest.json");
        AchievementOutputBoundary presenter = new AchievementOutputBoundary() {
            @Override
            public void present(AchievementOutputData responseModel) {
                assertNotNull(responseModel);
                assertEquals(2, responseModel.getUnlockedAchievements().size());
                Achievement achievement = responseModel.getUnlockedAchievements().get(0);
                achievement.unlock();
                assertEquals(true, achievement.isUnlocked());
                assertEquals("1 Time Learned", achievement.getName());
                assertEquals("You have learned 1 time!", achievement.getDescription());
                assertEquals(false, achievement.getIconUnicode().isBlank());
                assertEquals("A1", achievement.getId());
            }
        };

        AchievementInteractor interactor = new AchievementInteractor(presenter, mockDataAccess);

        AchievementInputData input = new AchievementInputData("AchievementTester");

        interactor.evaluate(input);
    }
}