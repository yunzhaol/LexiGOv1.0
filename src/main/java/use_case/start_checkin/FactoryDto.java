package use_case.start_checkin;

import entity.CommonCardFactory;
import entity.WordDeckFactory;

public class FactoryDto {
    private final WordDeckFactory wordDeckFactory;
    private final CommonCardFactory commonCardFactory;

    public FactoryDto(WordDeckFactory wordDeckFactory, CommonCardFactory commonCardFactory) {
        this.wordDeckFactory = wordDeckFactory;
        this.commonCardFactory = commonCardFactory;
    }

    public CommonCardFactory getCommonCardFactory() {
        return commonCardFactory;
    }

    public WordDeckFactory getWordDeckFactory() {
        return wordDeckFactory;
    }
}
