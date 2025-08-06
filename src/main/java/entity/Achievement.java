package entity;

import java.time.LocalDate;

/**
 * A badge-like achievement that a user can unlock by meeting specific criteria,
 * such as a 5-day streak or learning 100 vocabulary words.
 */
public final class Achievement {

    private final String id;
    private final String name;
    private final String description;
    private final String iconUnicode;
    private LocalDate dateUnlocked;

    /**
     * Constructs a new Achievement. By default it‘s locked (dateUnlocked is null).
     *
     * @param id            Internal ID
     * @param name          Display name
     * @param description   Detailed description
     * @param iconUnicode   Icon or emoji representing the badge
     */
    public Achievement(String id, String name, String description, String iconUnicode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconUnicode = iconUnicode;
        this.dateUnlocked = null;
    }

    // Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUnicode() {
        return iconUnicode;
    }

    public boolean isUnlocked() {
        return dateUnlocked != null;
    }

    /**
     * Unlocks the achievement with the current date.
     */
    public void unlock() {
        if (this.dateUnlocked == null) {
            this.dateUnlocked = LocalDate.now();
        }
    }

}
