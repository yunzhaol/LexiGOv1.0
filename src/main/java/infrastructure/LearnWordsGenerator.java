package infrastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import entity.LearnRecord;
import entity.WordBook;
import use_case.start_checkin.LearnDeckGenerator;

public class LearnWordsGenerator implements LearnDeckGenerator {

    @Override
    public List<UUID> generate(WordBook wordBook, List<LearnRecord> history, String length) {
        final List<UUID> uuidsTotal = wordBook.getWordIds();
        final List<UUID> uuidsLearned = new ArrayList<>();
        for (LearnRecord record : history) {
            for (UUID id : record.getLearnedWordIds()) {
                uuidsLearned.add(id);
            }
        }
        final Set<UUID> learnedSet = new HashSet<>(uuidsLearned);

        final List<UUID> candidates = uuidsTotal.stream()
                .filter(id -> !learnedSet.contains(id))
                .collect(Collectors.toList());
        List<UUID> results = new ArrayList<>();
        if (candidates.size() <= Integer.parseInt(length)) {
            results = candidates;
        }
        else {
            Collections.shuffle(candidates);
            results = candidates.subList(0, Integer.parseInt(length));
        }
        return results;
    }
}
