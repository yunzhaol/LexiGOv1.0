package entity;

import java.util.UUID;

public interface Word {
    /**
     * Returns the unique identifier associated with this instance.
     *
     * @return a non-null {@link java.util.UUID} that uniquely identifies the object
     */
    UUID getId();

    /**
     * Returns the literal text value held by this instance.
     *
     * @return the text; never {@code null}
     */
    String getText();
}
