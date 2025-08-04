package entity;

import java.util.UUID;

public interface WordFactory {

    /**
     * Creates a new immutable {@link Word} instance.
     *
     * @param text   the literal word text to store; must not be {@code null}
     * @param wordId the unique identifier to assign; must not be {@code null}
     * @return a fully-initialised {@code Word}
     */
    Word create(String text, UUID wordId);
}

