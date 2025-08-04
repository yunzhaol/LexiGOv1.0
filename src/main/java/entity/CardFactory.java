package entity;

import java.util.UUID;

public interface CardFactory {
    /**
     * Creates a {@link Card} instance populated with the supplied data.
     *
     * @param wordId      the unique identifier of the word represented by this card;
     *                    must not be {@code null}
     * @param text        the source-language word or phrase; must not be {@code null}
     * @param translation the target-language translation; must not be {@code null}
     * @param example     an example sentence or phrase illustrating usage;
     *                    may be {@code null} or empty if not available
     * @return a fully initialised {@link Card}
     */
    Card create(UUID wordId, String text, String translation, String example);
}
