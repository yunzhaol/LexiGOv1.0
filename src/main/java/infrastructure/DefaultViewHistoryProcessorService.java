package infrastructure;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import entity.LearnRecord;
import entity.ViewHistoryEntity;
import use_case.viewhistory.ViewHistoryEntryData;
import use_case.viewhistory.ViewHistoryProcessorService;

/**
 * Default implementation of ViewHistoryProcessorService.
 * Similar to DefaultScoreSort in the rank module.
 */
public class DefaultViewHistoryProcessorService implements ViewHistoryProcessorService {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<ViewHistoryEntryData> processRecords(List<LearnRecord> records) {
        final List<ViewHistoryEntryData> results;
        if (records == null || records.isEmpty()) {
            results = new ArrayList<>();
        }
        else {
            // Sort records by end time and create initial entities with session number 0
            final List<ViewHistoryEntity> sortedEntries = records.stream()
                    .sorted(Comparator.comparing(LearnRecord::getEndTime))
                    .map(record -> {
                        return new ViewHistoryEntity(
                                record.getUsername(),
                                record.getEndTime(),
                                record.getLearnedWordIds(),
                                0
                        );
                    })
                    .collect(Collectors.toList());

            // Assign sequential session numbers starting from 1 and convert to ViewHistoryEntryData
            results = new ArrayList<>();
            for (int i = 0; i < sortedEntries.size(); i++) {
                final ViewHistoryEntity entry = sortedEntries.get(i);
                final ViewHistoryEntryData entryData = new ViewHistoryEntryData(
                        i + 1,
                        entry.getEndTime().format(FORMATTER),
                        entry.getWordsCount()
                );
                results.add(entryData);
            }
        }
        return results;
    }
}
