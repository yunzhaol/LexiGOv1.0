package entity;

import java.util.List;
import java.util.UUID;

public interface WordBook {

    /**
     * Retrieves the immutable list of word identifiers contained in this book.
     *
     * @return an unmodifiable {@link java.util.List} of {@link java.util.UUID}s
     */
    List<UUID> getWordIds();
}
