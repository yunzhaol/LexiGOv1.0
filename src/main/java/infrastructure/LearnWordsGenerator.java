package infrastructure;

import entity.LearnRecord;
import entity.WordBook;
import use_case.start_checkin.LearnDeckGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class LearnWordsGenerator implements LearnDeckGenerator {

    @Override
    public List<UUID> generate(WordBook wordBook, List<LearnRecord> history, String length) {
        List<UUID> uuidsTotal = wordBook.getWordIds();
        List<UUID> uuidsLearned = new ArrayList<>();
        for (LearnRecord record : history) {
            for (UUID id : record.getLearnedWordIds()) {
                uuidsLearned.add(id);
            }
        }
        Set<UUID> learnedSet = new HashSet<>(uuidsLearned);

        List<UUID> candidates = uuidsTotal.stream()
                .filter(id -> !learnedSet.contains(id))
                .collect(Collectors.toList());

        if (candidates.size() <= Integer.parseInt(length)) {
            return candidates;
        }

        Collections.shuffle(candidates);
        return candidates.subList(0, Integer.parseInt(length));
    }
}
