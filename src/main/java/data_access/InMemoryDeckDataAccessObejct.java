package data_access;

import java.util.ArrayList;
import java.util.List;

import entity.CommonCard;
import entity.WordDeck;
import use_case.finish_checkin.UserDeckGetTextDataAccessInterface;
import use_case.start_checkin.UserCheckInDeckAccessInterface;
import use_case.studysession.UserDeckgetterDataAccessInterface;
import use_case.studysession.word_detail.WordDetailDataAccessInterface;

public class InMemoryDeckDataAccessObejct implements UserDeckGetTextDataAccessInterface,
        UserCheckInDeckAccessInterface,
        UserDeckgetterDataAccessInterface,
        WordDetailDataAccessInterface {

    private List<CommonCard> cardList = new ArrayList<>();

    @Override
    public List<CommonCard> getWordDeck() {
        return cardList;
    }

    @Override
    public void save(WordDeck deck) {
        cardList = deck.getWordDeck();
    }

    @Override
    public String getText(int index) {
        return getTextHelper(index);
    }

    private String getTextHelper(int index) {
        return cardList.get(index).getText();
    }

    @Override
    public String getTranslation(int index) {
        return getTranslationHelper(index);
    }

    private String getTranslationHelper(int index) {
        return cardList.get(index).getTranslation();
    }

    @Override
    public String getExample(int index) {
        return getExampleHelper(index);
    }

    private String getExampleHelper(int index) {
        return cardList.get(index).getExample();
    }
}
