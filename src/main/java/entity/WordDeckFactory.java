package entity;

import java.util.List;

public interface WordDeckFactory {

    WordDeck create(List<CommonCard> cards);
}
