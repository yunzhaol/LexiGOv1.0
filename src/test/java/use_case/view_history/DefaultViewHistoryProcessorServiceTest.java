package use_case.view_history;

import entity.LearnRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import infrastructure.DefaultViewHistoryProcessorService;
import use_case.viewhistory.ViewHistoryEntryData;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test for DefaultViewHistoryProcessorService
 */
class DefaultViewHistoryProcessorServiceTest {

    private DefaultViewHistoryProcessorService processor;

    @BeforeEach
    void setUp() {
        processor = new DefaultViewHistoryProcessorService();
    }

    @Test
    void shouldProcessRecordsInChronologicalOrder() {
        // Given - records in reverse order
        LocalDateTime time1 = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime time2 = LocalDateTime.of(2025, 1, 1, 11, 0);

        LearnRecord record1 = createMockRecord("user", time1, 2);
        LearnRecord record2 = createMockRecord("user", time2, 3);

        List<LearnRecord> records = Arrays.asList(record2, record1); // Out of order

        // When
        List<ViewHistoryEntryData> result = processor.processRecords(records);

        // Then
        assertEquals(2, result.size());

        // Should be sorted by time (record1 first, then record2)
        assertEquals(1, result.get(0).getSessionNumber());
        assertEquals("2025-01-01 10:00:00", result.get(0).getEndTime());
        assertEquals(2, result.get(0).getWordsCount());

        assertEquals(2, result.get(1).getSessionNumber());
        assertEquals("2025-01-01 11:00:00", result.get(1).getEndTime());
        assertEquals(3, result.get(1).getWordsCount());
    }

    @Test
    void shouldHandleEmptyRecords() {
        List<ViewHistoryEntryData> result = processor.processRecords(new ArrayList<>());
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleNullRecords() {
        List<ViewHistoryEntryData> result = processor.processRecords(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleSingleRecord() {
        LearnRecord record = createMockRecord("user", LocalDateTime.of(2025, 1, 1, 10, 0), 5);

        List<ViewHistoryEntryData> result = processor.processRecords(Arrays.asList(record));

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getSessionNumber());
        assertEquals("2025-01-01 10:00:00", result.get(0).getEndTime());
        assertEquals(5, result.get(0).getWordsCount());
    }

    @Test
    void shouldHandleRecordsWithSameTime() {
        LocalDateTime time = LocalDateTime.of(2025, 1, 1, 10, 0);
        LearnRecord record1 = createMockRecord("user", time, 1);
        LearnRecord record2 = createMockRecord("user", time, 2);

        List<ViewHistoryEntryData> result = processor.processRecords(Arrays.asList(record1, record2));

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getSessionNumber());
        assertEquals(2, result.get(1).getSessionNumber());
    }

    @Test
    void shouldHandleZeroWordCounts() {
        LearnRecord record = createMockRecord("user", LocalDateTime.of(2025, 1, 1, 10, 0), 0);

        List<ViewHistoryEntryData> result = processor.processRecords(Arrays.asList(record));

        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getWordsCount());
    }

    @Test
    void shouldFormatDateCorrectly() {
        LocalDateTime preciseTime = LocalDateTime.of(2025, 12, 25, 14, 30, 45);
        LearnRecord record = createMockRecord("user", preciseTime, 1);

        List<ViewHistoryEntryData> result = processor.processRecords(Arrays.asList(record));

        assertEquals("2025-12-25 14:30:45", result.get(0).getEndTime());
    }

    @Test
    void shouldAssignSequentialSessionNumbers() {
        List<LearnRecord> records = Arrays.asList(
                createMockRecord("user", LocalDateTime.of(2025, 1, 1, 10, 0), 1),
                createMockRecord("user", LocalDateTime.of(2025, 1, 1, 11, 0), 2),
                createMockRecord("user", LocalDateTime.of(2025, 1, 1, 12, 0), 3)
        );

        List<ViewHistoryEntryData> result = processor.processRecords(records);

        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getSessionNumber());
        assertEquals(2, result.get(1).getSessionNumber());
        assertEquals(3, result.get(2).getSessionNumber());
    }

    private LearnRecord createMockRecord(String username, LocalDateTime endTime, int wordCount) {
        LearnRecord record = mock(LearnRecord.class);
        when(record.getUsername()).thenReturn(username);
        when(record.getEndTime()).thenReturn(endTime);

        List<UUID> wordIds = new ArrayList<>();
        for (int i = 0; i < wordCount; i++) {
            wordIds.add(UUID.randomUUID());
        }
        when(record.getLearnedWordIds()).thenReturn(wordIds);

        return record;
    }
}