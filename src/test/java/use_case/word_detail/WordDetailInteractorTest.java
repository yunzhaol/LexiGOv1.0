package use_case.word_detail;

import data_access.InMemoryDeckDataAccessObejct;
import entity.CommonCard;
import entity.CommonWordDeck;
import entity.WordDeck;
import org.junit.jupiter.api.Test;
import use_case.studysession.word_detail.WordDetailInputData;
import use_case.studysession.word_detail.WordDetailInteractor;
import use_case.studysession.word_detail.WordDetailOutputBoundary;
import use_case.studysession.word_detail.WordDetailOutputData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class WordDetailInteractorTest {

    @Test
    void successTest() {
        WordDetailInputData inputData = new WordDetailInputData("1");
        InMemoryDeckDataAccessObejct deckDAO = new InMemoryDeckDataAccessObejct();

        CommonCard card = new CommonCard(UUID.fromString("7fc55970-6bf1-462c-b644-a8c75abf73c2"),"test", "测试", "test help");
        List<CommonCard> cards = new ArrayList<>();
        cards.add(card);
        WordDeck wordDeck = new CommonWordDeck(cards);
        deckDAO.save(wordDeck);
        // This creates a successPresenter that tests whether the test case is as we expect.
        WordDetailOutputBoundary successPresenter = new WordDetailOutputBoundary() {
            @Override
            public void prepareSuccessView(WordDetailOutputData out) {
                assertEquals(card.getExample(), out.getExample());
                assertEquals(card.getTranslation(), out.getTranslation());
                assertEquals(UUID.fromString("7fc55970-6bf1-462c-b644-a8c75abf73c2"), card.getWordId());
            }

            @Override
            public void switchTologgedView() {

            }

            @Override
            public void switchToStudySessionView() {

            }
        };

        WordDetailInteractor interactor = new WordDetailInteractor(successPresenter, deckDAO);
        interactor.execute(inputData);
    }

    @Test
    void switchTest() {
        WordDetailInputData inputData = new WordDetailInputData("1");
        InMemoryDeckDataAccessObejct deckDAO = new InMemoryDeckDataAccessObejct();

        CommonCard card = new CommonCard(UUID.fromString("7fc55970-6bf1-462c-b644-a8c75abf73c2"),"test", "测试", "test help");
        List<CommonCard> cards = new ArrayList<>();
        cards.add(card);
        WordDeck wordDeck = new CommonWordDeck(cards);
        deckDAO.save(wordDeck);
        // This creates a successPresenter that tests whether the test case is as we expect.
        WordDetailOutputBoundary successPresenter = new WordDetailOutputBoundary() {
            @Override
            public void prepareSuccessView(WordDetailOutputData out) {
            }

            @Override
            public void switchTologgedView() {

            }

            @Override
            public void switchToStudySessionView() {
                assertEquals("test", card.getText());
            }
        };

        WordDetailInteractor interactor = new WordDetailInteractor(successPresenter, deckDAO);
        interactor.switchToStudySessionView();
        interactor.switchTologgedView();
    }
}