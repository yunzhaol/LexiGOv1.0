package data_access;

import entity.CommonCard;
import entity.WordDeck;
import use_case.finish_checkin.UserDeckGetTextDataAccessInterface;
import use_case.start_checkin.UserCheckInDeckAccessInterface;
import use_case.studysession.UserDeckgetterDataAccessInterface;
import use_case.studysession.word_detail.WordDetailDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

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
    public String getTranslation(int i) {
        return getTranslationHelper(i);
    }

    private String getTranslationHelper(int i) {
        return cardList.get(i).getTranslation();
    }

    @Override
    public String getExample(int i) {
        return getExampleHelper(i);
    }

    private String getExampleHelper(int i) {
        return cardList.get(i).getExample();
    }
}
