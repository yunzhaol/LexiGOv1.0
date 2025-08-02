package use_case.rank;

import data_access.JsonUserRecordDataAccessObject;
import infrastructure.DefaultLeaderboardSelector;
import infrastructure.DefaultScoreSort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RankInteractorTest {

    @Test
    void successTest() {
        RankInputData inputData = new RankInputData("test01");
        JsonUserRecordDataAccessObject dao = new JsonUserRecordDataAccessObject("src/test/resources/data/rankTest.json");

        // This creates a successPresenter that tests whether the test case is as we expect.
        RankOutputBoundary successPresenter = new RankOutputBoundary() {
            @Override
            public void prepareSuccessView(RankOutputData rankOutputData) {
                assertEquals("test01", rankOutputData.currentUser());
                assertEquals(1, rankOutputData.myPosition());
                UserScore top1 = rankOutputData.leaderboard().get(0);
                assertEquals("test01", top1.getUsername());
                assertEquals(1, top1.getRank());
                assertEquals(6, top1.getScore());
            }
        };

        RankInteractor interactor = new RankInteractor(dao, successPresenter, new DefaultLeaderboardSelector(), new DefaultScoreSort(),10);
        interactor.execute(inputData);
    }
}