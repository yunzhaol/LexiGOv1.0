package entity;

import java.util.UUID;

public interface Card {
    /**
     * Returns the unique identifier of the word represented by this card.
     *
     * @return a non-null {@link java.util.UUID}
     */
    UUID getWordId();

    /**
     * Returns the source-language text shown on the front of the card.
     *
     * @return the word or phrase; never {@code null}
     */
    String getText();

    /**
     * Returns the target-language translation shown on the back of the card.
     *
     * @return the translation; never {@code null}
     */
    String getTranslation();

    /**
     * Returns an example sentence or phrase illustrating usage.
     *
     * @return the example text, or an empty string if none is available
     */
    String getExample();

}

