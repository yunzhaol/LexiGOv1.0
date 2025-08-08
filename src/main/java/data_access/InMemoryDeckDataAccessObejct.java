package data_access;

import java.util.List;

import entity.Card;
import entity.WordDeck;
import use_case.finish_checkin.UserDeckGetTextDataAccessInterface;
import use_case.start_checkin.UserCheckInDeckAccessInterface;
import use_case.studysession.UserDeckgetterDataAccessInterface;
import use_case.studysession.word_detail.WordDetailDataAccessInterface;

public class InMemoryDeckDataAccessObejct implements UserDeckGetTextDataAccessInterface,
        UserCheckInDeckAccessInterface,
        UserDeckgetterDataAccessInterface,
        WordDetailDataAccessInterface {

    private WordDeck wordDeck;

    @Override
    public List<Card> getWordDeck() {
        return wordDeck.getWordDeck();
    }

    @Override
    public void save(WordDeck deck) {
        wordDeck = deck;
    }

    @Override
    public String getText(int index) {
        return getTextHelper(index);
    }

    private String getTextHelper(int index) {
        return wordDeck.getWordDeck().get(index).getText();
    }

    @Override
    public String getTranslation(int index) {
        return getTranslationHelper(index);
    }

    private String getTranslationHelper(int index) {
        return wordDeck.getWordDeck().get(index).getTranslation();
    }

    @Override
    public String getExample(int index) {
        return getExampleHelper(index);
    }

    private String getExampleHelper(int index) {
        return wordDeck.getWordDeck().get(index).getExample();
    }
}
