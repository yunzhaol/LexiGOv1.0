package entity;

import java.util.List;
import java.util.UUID;

public interface WordBookFactory {

    /**
     * Creates a {@link WordBook} pre-populated with the supplied word IDs.
     *
     * @param name            the book’s display name; must not be {@code null}
     * @param initialwordids  the initial set of word identifiers to include;
     *                        must not be {@code null} and may be empty
     * @return a fully initialised {@code WordBook} instance
     */
    WordBook create(String name, List<UUID> initialwordids);
}
