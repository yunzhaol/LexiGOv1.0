package use_case.finish_checkin;

import data_access.InMemoryDeckDataAccessObejct;
import data_access.JsonUserRecordDataAccessObject;
import entity.CommonCard;
import entity.CommonLearnRecordFactory;
import entity.CommonWordDeck;
import entity.WordDeck;
import infrastructure.TimeGenerator;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FinishInInteractorTest {
    @Test
    public void SuccessTest() {
        InMemoryDeckDataAccessObejct deckDAO = new InMemoryDeckDataAccessObejct();

        CommonCard card = new CommonCard(UUID.fromString("7fc55970-6bf1-462c-b644-a8c75abf73c2"),"test", "测试", "test help");
        List<CommonCard> cards = new ArrayList<>();
        cards.add(card);
        WordDeck wordDeck = new CommonWordDeck(cards);
        deckDAO.save(wordDeck);
        JsonUserRecordDataAccessObject dao = new JsonUserRecordDataAccessObject("src/test/resources/data/finishTest.json");
        FinishCheckInInputData input = new FinishCheckInInputData("test01");

        FinishCheckInOutputBoundary presenter = new FinishCheckInOutputBoundary() {
            @Override
            public void prepareSuccessView() {
                assertEquals(1, deckDAO.getWordDeck().size());
            }
        };
        FinishCheckInInteractor interactor = new FinishCheckInInteractor(presenter, dao, deckDAO, new CommonLearnRecordFactory(), new TimeGenerator());
        interactor.execute(input);

    }
}
