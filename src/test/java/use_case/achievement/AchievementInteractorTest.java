//package use_case.achievement;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class AchievementInteractorTest {
//
//    private MockUserProgressDataAccess mockDataAccess;
//    private CapturingPresenter mockPresenter;
//    private AchievementInteractor interactor;
//
//    @BeforeEach
//    void setUp() {
//        mockDataAccess = new MockUserProgressDataAccess();
//        mockPresenter = new CapturingPresenter();
//        interactor = new AchievementInteractor(mockPresenter, mockDataAccess);
//    }
//
//    @Test
//    void testNoAchievementsUnlocked() {
//        mockDataAccess.setStreak(0);
//        mockDataAccess.setWordsLearned(0);
//
//        AchievementInputData input = new AchievementInputData("user1");
//        interactor.evaluate(input);
//
//        assertTrue(mockPresenter.getUnlocked().isEmpty());
//    }
//
//    @Test
//    void testAllAchievementsUnlocked() {
//        mockDataAccess.setStreak(10);
//        mockDataAccess.setWordsLearned(10);
//
//        AchievementInputData input = new AchievementInputData("user1");
//        interactor.evaluate(input);
//
//        List<String> unlocked = mockPresenter.getUnlocked();
//        assertTrue(unlocked.contains("👋 Nice to Meet You"));
//        assertTrue(unlocked.contains("🔥 5-Day Streak"));
//        assertTrue(unlocked.contains("🔥 10-Day Streak"));
//        assertTrue(unlocked.contains("👋 First Word I Learned"));
//        assertTrue(unlocked.contains("🎉 10 Words Learned"));
//        assertEquals(5, unlocked.size());
//    }
//
//    @Test
//    void testOnlyFirstWordAchievementUnlocked() {
//        mockDataAccess.setStreak(0);
//        mockDataAccess.setWordsLearned(1);
//
//        AchievementInputData input = new AchievementInputData("user1");
//        interactor.evaluate(input);
//
//        List<String> unlocked = mockPresenter.getUnlocked();
//        assertEquals(1, unlocked.size());
//        assertTrue(unlocked.contains("👋 First Word I Learned"));
//    }
//
//    // --- Mock classes below ---
//
//    static class MockUserProgressDataAccess implements UserProgressDataAccessInterface {
//        private int streak;
//        private int wordsLearned;
//
//        void setStreak(int s) {
//            this.streak = s;
//        }
//
//        void setWordsLearned(int w) {
//            this.wordsLearned = w;
//        }
//
//        @Override
//        public int getStreak(String userId) {
//            return streak;
//        }
//
//        @Override
//        public int getWordCount(String userId) {
//            return wordsLearned;
//        }
//    }
//
//    static class CapturingPresenter implements AchievementOutputBoundary {
//        private List<String> unlocked;
//
//        @Override
//        public void present(AchievementOutputData responseModel) {
//            this.unlocked = responseModel.getUnlockedAchievements();
//        }
//
//        List<String> getUnlocked() {
//            return unlocked;
//        }
//    }
//}