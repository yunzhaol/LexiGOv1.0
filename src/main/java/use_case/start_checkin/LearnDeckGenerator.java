package use_case.start_checkin;

import java.util.List;
import java.util.UUID;

import entity.LearnRecord;
import entity.WordBook;

public interface LearnDeckGenerator {
    /**
     * Generates a list of flashcard UUIDs for a learning session.
     *
     * @param wordBook the WordBook containing the vocabulary to learn
     * @param history  the list of past LearnRecord entries to consider
     * @param length   the desired deck length (e.g., "short", "medium", "long")
     * @return a list of UUIDs representing the generated learning deck
     */
    List<UUID> generate(WordBook wordBook, List<LearnRecord> history, String length);
}
