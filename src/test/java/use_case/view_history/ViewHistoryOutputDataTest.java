package use_case.view_history;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import use_case.viewhistory.ViewHistoryEntryData;
import use_case.viewhistory.ViewHistoryOutputData;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ViewHistoryOutputData
 */
class ViewHistoryOutputDataTest {

    @Test
    @DisplayName("Should create output data with all fields")
    void shouldCreateOutputDataWithAllFields() {
        // Given
        String username = "testUser";
        List<ViewHistoryEntryData> sessions = Arrays.asList(
                new ViewHistoryEntryData(1, "2025-01-01 10:00:00", 5),
                new ViewHistoryEntryData(2, "2025-01-01 11:00:00", 3)
        );
        int totalSessions = 2;
        int totalWords = 8;

        // When
        ViewHistoryOutputData outputData = new ViewHistoryOutputData(username, sessions, totalSessions, totalWords);

        // Then
        assertEquals(username, outputData.getUsername());
        assertEquals(sessions, outputData.getSessions());
        assertEquals(totalSessions, outputData.getTotalSessions());
        assertEquals(totalWords, outputData.getTotalWords());
    }

    @Test
    @DisplayName("Should handle null username")
    void shouldHandleNullUsername() {
        // Given
        List<ViewHistoryEntryData> sessions = new ArrayList<>();

        // When
        ViewHistoryOutputData outputData = new ViewHistoryOutputData(null, sessions, 0, 0);

        // Then
        assertNull(outputData.getUsername());
    }

    @Test
    @DisplayName("Should handle empty username")
    void shouldHandleEmptyUsername() {
        // Given
        String emptyUsername = "";
        List<ViewHistoryEntryData> sessions = new ArrayList<>();

        // When
        ViewHistoryOutputData outputData = new ViewHistoryOutputData(emptyUsername, sessions, 0, 0);

        // Then
        assertEquals(emptyUsername, outputData.getUsername());
    }

    @Test
    @DisplayName("Should handle null sessions list")
    void shouldHandleNullSessionsList() {
        // When
        ViewHistoryOutputData outputData = new ViewHistoryOutputData("testUser", null, 0, 0);

        // Then
        assertNull(outputData.getSessions());
    }

    @Test
    @DisplayName("Should handle empty sessions list")
    void shouldHandleEmptySessionsList() {
        // Given
        String username = "testUser";
        List<ViewHistoryEntryData> sessions = new ArrayList<>();

        // When
        ViewHistoryOutputData outputData = new ViewHistoryOutputData(username, sessions, 0, 0);

        // Then
        assertEquals(username, outputData.getUsername());
        assertTrue(outputData.getSessions().isEmpty());
        assertEquals(0, outputData.getTotalSessions());
        assertEquals(0, outputData.getTotalWords());
    }

    @Test
    @DisplayName("Should handle single session")
    void shouldHandleSingleSession() {
        // Given
        String username = "testUser";
        List<ViewHistoryEntryData> sessions = Arrays.asList(
                new ViewHistoryEntryData(1, "2025-01-01 10:00:00", 5)
        );

        // When
        ViewHistoryOutputData outputData = new ViewHistoryOutputData(username, sessions, 1, 5);

        // Then
        assertEquals(1, outputData.getSessions().size());
        assertEquals(1, outputData.getTotalSessions());
        assertEquals(5, outputData.getTotalWords());
    }

    @Test
    @DisplayName("Should handle large number of sessions")
    void shouldHandleLargeNumberOfSessions() {
        // Given
        String username = "testUser";
        List<ViewHistoryEntryData> sessions = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            sessions.add(new ViewHistoryEntryData(i, "2025-01-01 10:00:00", 1));
        }

        // When
        ViewHistoryOutputData outputData = new ViewHistoryOutputData(username, sessions, 1000, 1000);

        // Then
        assertEquals(1000, outputData.getSessions().size());
        assertEquals(1000, outputData.getTotalSessions());
        assertEquals(1000, outputData.getTotalWords());
    }

    @Test
    @DisplayName("Should handle zero totals")
    void shouldHandleZeroTotals() {
        // Given
        String username = "testUser";
        List<ViewHistoryEntryData> sessions = new ArrayList<>();

        // When
        ViewHistoryOutputData outputData = new ViewHistoryOutputData(username, sessions, 0, 0);

        // Then
        assertEquals(0, outputData.getTotalSessions());
        assertEquals(0, outputData.getTotalWords());
    }

    @Test
    @DisplayName("Should handle negative totals")
    void shouldHandleNegativeTotals() {
        // Given
        String username = "testUser";
        List<ViewHistoryEntryData> sessions = new ArrayList<>();

        // When
        ViewHistoryOutputData outputData = new ViewHistoryOutputData(username, sessions, -1, -1);

        // Then
        assertEquals(-1, outputData.getTotalSessions());
        assertEquals(-1, outputData.getTotalWords());
    }

    @Test
    @DisplayName("Should handle mismatched totals and sessions")
    void shouldHandleMismatchedTotalsAndSessions() {
        // Given - sessions list has 2 items but totals say 3
        String username = "testUser";
        List<ViewHistoryEntryData> sessions = Arrays.asList(
                new ViewHistoryEntryData(1, "2025-01-01 10:00:00", 5),
                new ViewHistoryEntryData(2, "2025-01-01 11:00:00", 3)
        );

        // When
        ViewHistoryOutputData outputData = new ViewHistoryOutputData(username, sessions, 3, 10);

        // Then - should store what was provided, even if inconsistent
        assertEquals(2, outputData.getSessions().size());
        assertEquals(3, outputData.getTotalSessions());
        assertEquals(10, outputData.getTotalWords());
    }

    @Test
    @DisplayName("Should preserve session order")
    void shouldPreserveSessionOrder() {
        // Given
        String username = "testUser";
        List<ViewHistoryEntryData> sessions = Arrays.asList(
                new ViewHistoryEntryData(3, "2025-01-01 12:00:00", 1),
                new ViewHistoryEntryData(1, "2025-01-01 10:00:00", 5),
                new ViewHistoryEntryData(2, "2025-01-01 11:00:00", 3)
        );

        // When
        ViewHistoryOutputData outputData = new ViewHistoryOutputData(username, sessions, 3, 9);

        // Then - should preserve the original order
        List<ViewHistoryEntryData> retrievedSessions = outputData.getSessions();
        assertEquals(3, retrievedSessions.get(0).getSessionNumber());
        assertEquals(1, retrievedSessions.get(1).getSessionNumber());
        assertEquals(2, retrievedSessions.get(2).getSessionNumber());
    }

    @Test
    @DisplayName("Should handle sessions with same session number")
    void shouldHandleSessionsWithSameSessionNumber() {
        // Given
        String username = "testUser";
        List<ViewHistoryEntryData> sessions = Arrays.asList(
                new ViewHistoryEntryData(1, "2025-01-01 10:00:00", 5),
                new ViewHistoryEntryData(1, "2025-01-01 10:00:00", 3)
        );

        // When
        ViewHistoryOutputData outputData = new ViewHistoryOutputData(username, sessions, 2, 8);

        // Then
        assertEquals(2, outputData.getSessions().size());
        assertEquals(1, outputData.getSessions().get(0).getSessionNumber());
        assertEquals(1, outputData.getSessions().get(1).getSessionNumber());
    }
}