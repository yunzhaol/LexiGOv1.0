package infrastructure;

import entity.LearnRecord;
import entity.WordBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LearnWordsGeneratorTest {

    private LearnWordsGenerator generator;

    private final List<UUID> allIds = Collections.nCopies(10, 0)
            .stream().map(i -> UUID.randomUUID()).collect(Collectors.toList());
    private final List<UUID> learnedIds = allIds.subList(0, 3);

    private WordBook wordBook;
    private List<LearnRecord> history;

    @BeforeEach
    void setUp() {
        generator = new LearnWordsGenerator();

        wordBook = mock(WordBook.class);
        when(wordBook.getWordIds()).thenReturn(allIds);

        LearnRecord record = mock(LearnRecord.class);
        when(record.getLearnedWordIds()).thenReturn(learnedIds);
        history = Collections.singletonList(record);
    }

    @Test
    void returns_all_when_candidate_count_not_exceed_limit() {
        List<UUID> result = generator.generate(wordBook, history, "7");

        List<UUID> expected = allIds.stream()
                .filter(id -> !learnedIds.contains(id))
                .collect(Collectors.toList());

        assertEquals(expected, result, ">= limit");
    }

    @Test
    void returns_random_subset_when_candidate_exceed_limit() {

        List<UUID> result = generator.generate(wordBook, history, "5");

        assertEquals(5, result.size(), "= limit");
        assertTrue(result.stream().noneMatch(learnedIds::contains), "no words learnt should be here");
        assertEquals(result.size(), new HashSet<>(result).size(), "no repeats");
    }
}
