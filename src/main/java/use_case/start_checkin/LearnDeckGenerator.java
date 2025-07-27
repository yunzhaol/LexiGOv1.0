package use_case.start_checkin;

import entity.LearnRecord;
import entity.WordBook;

import java.util.List;
import java.util.UUID;

public interface LearnDeckGenerator {
    List<UUID> generate(WordBook wordBook, List<LearnRecord> history, String length);
}
