package use_case.viewhistory;

import entity.CommonLearnRecord;
import entity.LearnRecord;
import entity.ViewHistoryEntity;
import infrastructure.DefaultViewHistoryProcessorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.gateway.UserRecordDataAccessInterface;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for ViewHistoryInteractor
 */
class ViewHistoryInteractorTest {

    @Mock
    private UserRecordDataAccessInterface mockDataAccess;

    @Mock
    private ViewHistoryOutputBoundary mockPresenter;

    @Mock
    private ViewHistoryProcessorService mockProcessorService;

    private ViewHistoryInteractor interactor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        interactor = new ViewHistoryInteractor(mockDataAccess, mockPresenter, new DefaultViewHistoryProcessorService());
    }

    @Test
    @DisplayName("Should execute successfully with valid data")
    void shouldExecuteSuccessfullyWithValidData() {
        // Given
        String username = "testUser";
        ViewHistoryInputData inputData = new ViewHistoryInputData(username);

        LocalDateTime time1 = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime time2 = LocalDateTime.of(2025, 1, 1, 11, 0);

        LearnRecord record1 = createMockRecord(username, time1, 2);
        LearnRecord record2 = createMockRecord(username, time2, 1);

        List<LearnRecord> records = Arrays.asList(record1, record2);
        when(mockDataAccess.get(username)).thenReturn(records);

        // When
        interactor.execute(inputData);

        // Then
        verify(mockDataAccess).get(username);

        ArgumentCaptor<ViewHistoryOutputData> outputCaptor = ArgumentCaptor.forClass(ViewHistoryOutputData.class);
        verify(mockPresenter).prepareSuccessView(outputCaptor.capture());
        verify(mockPresenter, never()).prepareFailView(any(String.class));

        ViewHistoryOutputData capturedOutput = outputCaptor.getValue();
        assertEquals(username, capturedOutput.getUsername());
        assertEquals(2, capturedOutput.getTotalSessions());
        assertEquals(3, capturedOutput.getTotalWords());
        assertEquals(2, capturedOutput.getSessions().size());
    }

    @Test
    @DisplayName("Should handle empty records list")
    void shouldHandleEmptyRecordsList() {
        // Given
        String username = "testUser";
        ViewHistoryInputData inputData = new ViewHistoryInputData(username);

        when(mockDataAccess.get(username)).thenReturn(new ArrayList<>());

        // When
        interactor.execute(inputData);

        // Then
        verify(mockDataAccess).get(username);
        verify(mockPresenter).prepareFailView("No learning records found for user: " + username);
        verify(mockPresenter, never()).prepareSuccessView(any(ViewHistoryOutputData.class));
    }

    @Test
    @DisplayName("Should handle null username")
    void shouldHandleNullUsername() {
        // Given
        ViewHistoryInputData inputData = new ViewHistoryInputData(null);
        when(mockDataAccess.get(null)).thenReturn(new ArrayList<>());

        // When
        interactor.execute(inputData);

        // Then
        verify(mockDataAccess).get(null);
        verify(mockPresenter).prepareFailView("No learning records found for user: null");
    }

    @Test
    @DisplayName("Should handle empty string username")
    void shouldHandleEmptyStringUsername() {
        // Given
        String emptyUsername = "";
        ViewHistoryInputData inputData = new ViewHistoryInputData(emptyUsername);
        when(mockDataAccess.get(emptyUsername)).thenReturn(new ArrayList<>());

        // When
        interactor.execute(inputData);

        // Then
        verify(mockDataAccess).get(emptyUsername);
        verify(mockPresenter).prepareFailView("No learning records found for user: ");
    }

    @Test
    @DisplayName("Should calculate totals correctly")
    void shouldCalculateTotalsCorrectly() {
        // Given
        String username = "testUser";
        ViewHistoryInputData inputData = new ViewHistoryInputData(username);

        LocalDateTime time1 = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime time2 = LocalDateTime.of(2025, 1, 1, 11, 0);
        LocalDateTime time3 = LocalDateTime.of(2025, 1, 1, 12, 0);

        // 3 sessions: 3 words, 2 words, 1 word = 6 total words
        LearnRecord record1 = createMockRecord(username, time1, 3);
        LearnRecord record2 = createMockRecord(username, time2, 2);
        LearnRecord record3 = createMockRecord(username, time3, 1);

        when(mockDataAccess.get(username)).thenReturn(Arrays.asList(record1, record2, record3));

        // When
        interactor.execute(inputData);

        // Then
        ArgumentCaptor<ViewHistoryOutputData> outputCaptor = ArgumentCaptor.forClass(ViewHistoryOutputData.class);
        verify(mockPresenter).prepareSuccessView(outputCaptor.capture());

        ViewHistoryOutputData capturedOutput = outputCaptor.getValue();
        assertEquals(3, capturedOutput.getTotalSessions());
        assertEquals(6, capturedOutput.getTotalWords());
    }

    @Test
    @DisplayName("Should preserve session order after sorting")
    void shouldPreserveSessionOrderAfterSorting() {
        // Given - records in reverse chronological order
        String username = "testUser";
        ViewHistoryInputData inputData = new ViewHistoryInputData(username);

        LocalDateTime time1 = LocalDateTime.of(2025, 1, 1, 12, 0); // Latest
        LocalDateTime time2 = LocalDateTime.of(2025, 1, 1, 10, 0); // Earliest
        LocalDateTime time3 = LocalDateTime.of(2025, 1, 1, 11, 0); // Middle

        LearnRecord record1 = createMockRecord(username, time1, 1);
        LearnRecord record2 = createMockRecord(username, time2, 2);
        LearnRecord record3 = createMockRecord(username, time3, 3);

        // Input in wrong order
        when(mockDataAccess.get(username)).thenReturn(Arrays.asList(record1, record2, record3));

        // When
        interactor.execute(inputData);

        // Then
        ArgumentCaptor<ViewHistoryOutputData> outputCaptor = ArgumentCaptor.forClass(ViewHistoryOutputData.class);
        verify(mockPresenter).prepareSuccessView(outputCaptor.capture());

        ViewHistoryOutputData capturedOutput = outputCaptor.getValue();
        List<ViewHistoryEntryData> sessions = capturedOutput.getSessions();

        assertEquals(3, sessions.size());

        // Should be in chronological order
        assertEquals("2025-01-01 10:00:00", sessions.get(0).getEndTime()); // Earliest first
        assertEquals("2025-01-01 11:00:00", sessions.get(1).getEndTime()); // Middle second
        assertEquals("2025-01-01 12:00:00", sessions.get(2).getEndTime()); // Latest third

        // Session numbers should be sequential
        assertEquals(1, sessions.get(0).getSessionNumber());
        assertEquals(2, sessions.get(1).getSessionNumber());
        assertEquals(3, sessions.get(2).getSessionNumber());
    }

    @Test
    @DisplayName("Should handle single session correctly")
    void shouldHandleSingleSessionCorrectly() {
        // Given
        String username = "singleUser";
        ViewHistoryInputData inputData = new ViewHistoryInputData(username);

        LocalDateTime time = LocalDateTime.of(2025, 1, 1, 10, 0);
        LearnRecord record = createMockRecord(username, time, 5);

        when(mockDataAccess.get(username)).thenReturn(Arrays.asList(record));

        // When
        interactor.execute(inputData);

        // Then
        ArgumentCaptor<ViewHistoryOutputData> outputCaptor = ArgumentCaptor.forClass(ViewHistoryOutputData.class);
        verify(mockPresenter).prepareSuccessView(outputCaptor.capture());

        ViewHistoryOutputData capturedOutput = outputCaptor.getValue();
        assertEquals(1, capturedOutput.getTotalSessions());
        assertEquals(5, capturedOutput.getTotalWords());
        assertEquals(1, capturedOutput.getSessions().get(0).getSessionNumber());
    }

    @Test
    @DisplayName("Should handle zero word sessions")
    void shouldHandleZeroWordSessions() {
        // Given
        String username = "testUser";
        ViewHistoryInputData inputData = new ViewHistoryInputData(username);

        LocalDateTime time = LocalDateTime.of(2025, 1, 1, 10, 0);
        LearnRecord record = createMockRecord(username, time, 0);

        when(mockDataAccess.get(username)).thenReturn(Arrays.asList(record));

        // When
        interactor.execute(inputData);

        // Then
        ArgumentCaptor<ViewHistoryOutputData> outputCaptor = ArgumentCaptor.forClass(ViewHistoryOutputData.class);
        verify(mockPresenter).prepareSuccessView(outputCaptor.capture());

        ViewHistoryOutputData capturedOutput = outputCaptor.getValue();
        assertEquals(1, capturedOutput.getTotalSessions());
        assertEquals(0, capturedOutput.getTotalWords());
    }

    @Test
    @DisplayName("Should handle large dataset efficiently")
    void shouldHandleLargeDatasetEfficiently() {
        // Given
        String username = "testUser";
        ViewHistoryInputData inputData = new ViewHistoryInputData(username);

        List<LearnRecord> largeRecordSet = new ArrayList<>();
        LocalDateTime baseTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        int totalWords = 0;

        for (int i = 0; i < 1000; i++) {
            int wordCount = i % 10; // 0-9 words per session
            LearnRecord record = createMockRecord(username, baseTime.plusMinutes(i), wordCount);
            largeRecordSet.add(record);
            totalWords += wordCount;
        }

        when(mockDataAccess.get(username)).thenReturn(largeRecordSet);

        // When
        long startTime = System.currentTimeMillis();
        interactor.execute(inputData);
        long endTime = System.currentTimeMillis();

        // Then
        ArgumentCaptor<ViewHistoryOutputData> outputCaptor = ArgumentCaptor.forClass(ViewHistoryOutputData.class);
        verify(mockPresenter).prepareSuccessView(outputCaptor.capture());

        ViewHistoryOutputData capturedOutput = outputCaptor.getValue();
        assertEquals(1000, capturedOutput.getTotalSessions());
        assertEquals(totalWords, capturedOutput.getTotalWords());

        // Performance assertion
        assertTrue(endTime - startTime < 5000, "Processing should complete within 5 seconds");
    }

    @Test
    @DisplayName("Should handle records with same timestamp")
    void shouldHandleRecordsWithSameTimestamp() {
        // Given
        String username = "testUser";
        ViewHistoryInputData inputData = new ViewHistoryInputData(username);

        LocalDateTime time = LocalDateTime.of(2025, 1, 1, 10, 0);
        LearnRecord record1 = createMockRecord(username, time, 1);
        LearnRecord record2 = createMockRecord(username, time, 2);
        LearnRecord record3 = createMockRecord(username, time, 3);

        when(mockDataAccess.get(username)).thenReturn(Arrays.asList(record1, record2, record3));

        // When
        interactor.execute(inputData);

        // Then
        ArgumentCaptor<ViewHistoryOutputData> outputCaptor = ArgumentCaptor.forClass(ViewHistoryOutputData.class);
        verify(mockPresenter).prepareSuccessView(outputCaptor.capture());

        ViewHistoryOutputData capturedOutput = outputCaptor.getValue();
        assertEquals(3, capturedOutput.getTotalSessions());
        assertEquals(6, capturedOutput.getTotalWords());

        List<ViewHistoryEntryData> sessions = capturedOutput.getSessions();
        assertEquals(1, sessions.get(0).getSessionNumber());
        assertEquals(2, sessions.get(1).getSessionNumber());
        assertEquals(3, sessions.get(2).getSessionNumber());
    }

    @Test
    @DisplayName("Should verify data access interaction only once")
    void shouldVerifyDataAccessInteractionOnlyOnce() {
        // Given
        String username = "testUser";
        ViewHistoryInputData inputData = new ViewHistoryInputData(username);

        LocalDateTime time = LocalDateTime.of(2025, 1, 1, 10, 0);
        LearnRecord record = createMockRecord(username, time, 1);

        when(mockDataAccess.get(username)).thenReturn(Arrays.asList(record));

        // When
        interactor.execute(inputData);

        // Then
        verify(mockDataAccess, times(1)).get(username); // Should be called exactly once
        verify(mockPresenter, times(1)).prepareSuccessView(any(ViewHistoryOutputData.class));
        verify(mockPresenter, never()).prepareFailView(any(String.class));
    }

    @Test
    @DisplayName("Should handle mixed word counts")
    void shouldHandleMixedWordCounts() {
        // Given
        String username = "testUser";
        ViewHistoryInputData inputData = new ViewHistoryInputData(username);

        LocalDateTime time1 = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime time2 = LocalDateTime.of(2025, 1, 1, 11, 0);
        LocalDateTime time3 = LocalDateTime.of(2025, 1, 1, 12, 0);
        LocalDateTime time4 = LocalDateTime.of(2025, 1, 1, 13, 0);

        LearnRecord record1 = createMockRecord(username, time1, 0);     // No words
        LearnRecord record2 = createMockRecord(username, time2, 1);     // One word
        LearnRecord record3 = createMockRecord(username, time3, 100);   // Many words
        LearnRecord record4 = createMockRecord(username, time4, 0);     // No words again

        when(mockDataAccess.get(username)).thenReturn(Arrays.asList(record1, record2, record3, record4));

        // When
        interactor.execute(inputData);

        // Then
        ArgumentCaptor<ViewHistoryOutputData> outputCaptor = ArgumentCaptor.forClass(ViewHistoryOutputData.class);
        verify(mockPresenter).prepareSuccessView(outputCaptor.capture());

        ViewHistoryOutputData capturedOutput = outputCaptor.getValue();
        assertEquals(4, capturedOutput.getTotalSessions());
        assertEquals(101, capturedOutput.getTotalWords()); // 0 + 1 + 100 + 0

        List<ViewHistoryEntryData> sessions = capturedOutput.getSessions();
        assertEquals(0, sessions.get(0).getWordsCount());
        assertEquals(1, sessions.get(1).getWordsCount());
        assertEquals(100, sessions.get(2).getWordsCount());
        assertEquals(0, sessions.get(3).getWordsCount());
    }

    @Test
    @DisplayName("Should handle data access returning null")
    void shouldHandleDataAccessReturningNull() {
        // Given
        String username = "testUser";
        ViewHistoryInputData inputData = new ViewHistoryInputData(username);

        when(mockDataAccess.get(username)).thenReturn(null);

        // When & Then - Should throw NullPointerException
        assertThrows(NullPointerException.class, () -> {
            interactor.execute(inputData);
        });

        verify(mockDataAccess).get(username);
    }

    @Test
    @DisplayName("Should verify correct date formatting")
    void shouldVerifyCorrectDateFormatting() {
        // Given
        String username = "testUser";
        ViewHistoryInputData inputData = new ViewHistoryInputData(username);

        LocalDateTime preciseTime = LocalDateTime.of(2025, 12, 25, 14, 30, 45, 123456789);
        LearnRecord record = createMockRecord(username, preciseTime, 1);

        when(mockDataAccess.get(username)).thenReturn(Arrays.asList(record));

        // When
        interactor.execute(inputData);

        // Then
        ArgumentCaptor<ViewHistoryOutputData> outputCaptor = ArgumentCaptor.forClass(ViewHistoryOutputData.class);
        verify(mockPresenter).prepareSuccessView(outputCaptor.capture());

        ViewHistoryOutputData capturedOutput = outputCaptor.getValue();
        List<ViewHistoryEntryData> sessions = capturedOutput.getSessions();

        assertEquals("2025-12-25 14:30:45", sessions.get(0).getEndTime());
    }

    @Test
    void entityTest() {
        LocalDateTime time = LocalDateTime.of(2025, 1, 1, 10, 0);
        CommonLearnRecord record = new CommonLearnRecord("TEST", time, new ArrayList<>());
        assertEquals(time, record.getEndTime());
        assertEquals(0, record.getLearnedWordIds().size());

        ViewHistoryEntity viewHistoryEntity = new ViewHistoryEntity("TEST", time, new ArrayList<>(), 1);
        assertEquals(time, viewHistoryEntity.getEndTime());
        assertEquals(0, viewHistoryEntity.getLearnedWordIds().size());
        assertEquals(viewHistoryEntity.getUsername(),"TEST");
    }

    // Helper method
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