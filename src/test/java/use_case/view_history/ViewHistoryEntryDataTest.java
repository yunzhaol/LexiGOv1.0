package use_case.view_history;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import use_case.viewhistory.ViewHistoryEntryData;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ViewHistoryEntryData
 */
class ViewHistoryEntryDataTest {

    @Test
    @DisplayName("Should create entry data with all fields")
    void shouldCreateEntryDataWithAllFields() {
        // Given
        int sessionNumber = 1;
        String endTime = "2025-01-01 10:00:00";
        int wordsCount = 5;

        // When
        ViewHistoryEntryData entryData = new ViewHistoryEntryData(sessionNumber, endTime, wordsCount);

        // Then
        assertEquals(sessionNumber, entryData.getSessionNumber());
        assertEquals(endTime, entryData.getEndTime());
        assertEquals(wordsCount, entryData.getWordsCount());
    }

    @Test
    @DisplayName("Should handle zero session number")
    void shouldHandleZeroSessionNumber() {
        // When
        ViewHistoryEntryData entryData = new ViewHistoryEntryData(0, "2025-01-01 10:00:00", 5);

        // Then
        assertEquals(0, entryData.getSessionNumber());
    }

    @Test
    @DisplayName("Should handle negative session number")
    void shouldHandleNegativeSessionNumber() {
        // When
        ViewHistoryEntryData entryData = new ViewHistoryEntryData(-1, "2025-01-01 10:00:00", 5);

        // Then
        assertEquals(-1, entryData.getSessionNumber());
    }

    @Test
    @DisplayName("Should handle large session number")
    void shouldHandleLargeSessionNumber() {
        // Given
        int largeSessionNumber = Integer.MAX_VALUE;

        // When
        ViewHistoryEntryData entryData = new ViewHistoryEntryData(largeSessionNumber, "2025-01-01 10:00:00", 5);

        // Then
        assertEquals(largeSessionNumber, entryData.getSessionNumber());
    }

    @Test
    @DisplayName("Should handle null endTime")
    void shouldHandleNullEndTime() {
        // When
        ViewHistoryEntryData entryData = new ViewHistoryEntryData(1, null, 5);

        // Then
        assertNull(entryData.getEndTime());
    }

    @Test
    @DisplayName("Should handle empty endTime")
    void shouldHandleEmptyEndTime() {
        // Given
        String emptyEndTime = "";

        // When
        ViewHistoryEntryData entryData = new ViewHistoryEntryData(1, emptyEndTime, 5);

        // Then
        assertEquals(emptyEndTime, entryData.getEndTime());
    }

    @Test
    @DisplayName("Should handle different date formats")
    void shouldHandleDifferentDateFormats() {
        // Given
        String customDateFormat = "01/01/2025 10:00 AM";

        // When
        ViewHistoryEntryData entryData = new ViewHistoryEntryData(1, customDateFormat, 5);

        // Then
        assertEquals(customDateFormat, entryData.getEndTime());
    }

    @Test
    @DisplayName("Should handle zero words count")
    void shouldHandleZeroWordsCount() {
        // When
        ViewHistoryEntryData entryData = new ViewHistoryEntryData(1, "2025-01-01 10:00:00", 0);

        // Then
        assertEquals(0, entryData.getWordsCount());
    }

    @Test
    @DisplayName("Should handle negative words count")
    void shouldHandleNegativeWordsCount() {
        // When
        ViewHistoryEntryData entryData = new ViewHistoryEntryData(1, "2025-01-01 10:00:00", -1);

        // Then
        assertEquals(-1, entryData.getWordsCount());
    }

    @Test
    @DisplayName("Should handle large words count")
    void shouldHandleLargeWordsCount() {
        // Given
        int largeWordsCount = Integer.MAX_VALUE;

        // When
        ViewHistoryEntryData entryData = new ViewHistoryEntryData(1, "2025-01-01 10:00:00", largeWordsCount);

        // Then
        assertEquals(largeWordsCount, entryData.getWordsCount());
    }

    @Test
    @DisplayName("Should create multiple instances with different values")
    void shouldCreateMultipleInstancesWithDifferentValues() {
        // Given
        ViewHistoryEntryData entry1 = new ViewHistoryEntryData(1, "2025-01-01 10:00:00", 5);
        ViewHistoryEntryData entry2 = new ViewHistoryEntryData(2, "2025-01-01 11:00:00", 3);

        // Then
        assertNotEquals(entry1.getSessionNumber(), entry2.getSessionNumber());
        assertNotEquals(entry1.getEndTime(), entry2.getEndTime());
        assertNotEquals(entry1.getWordsCount(), entry2.getWordsCount());
    }
}