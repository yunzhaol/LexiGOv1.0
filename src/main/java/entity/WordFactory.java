package entity;

import java.util.UUID;

public interface WordFactory {

    Word create(String text, UUID wordId);
}

