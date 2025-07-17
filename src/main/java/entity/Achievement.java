package entity;

import java.time.LocalDate;
import java.util.Objects;

/**
 * A badge-like achievement that a user can unlock by meeting specific criteria,
 * such as a 5-day streak or learning 100 vocabulary words.
 */
public class Achievement {

    private final String id;              // Internal identifier
    private final String name;            // Display name, eg, "🔥 5-Day Streak"
    private final String description;     // Description for tooltip
    private final String iconUnicode;     // emoji icon
    private LocalDate dateUnlocked;       // Null if still locked

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

    public LocalDate getDateUnlocked() {
        return dateUnlocked;
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

    /**
     * Unlocks the achievement with a custom date (used in testing or backfill).
     *
     * @param date Date the achievement was unlocked
     */
    public void unlock(LocalDate date) {
        if (this.dateUnlocked == null) {
            this.dateUnlocked = date;
        }
    }

    // Equals and hashCode (based only on ID, which should be unique)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Achievement)) return false;
        Achievement that = (Achievement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Optional: String representation, may delete later

    @Override
    public String toString() {
        return (isUnlocked() ? "✅" : "🔒") +
                " " + name + " (" + id + ") — " +
                (isUnlocked() ? "Unlocked on " + dateUnlocked : "Locked");
    }
}