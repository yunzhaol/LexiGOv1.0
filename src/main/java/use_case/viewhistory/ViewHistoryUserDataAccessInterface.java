package use_case.viewhistory;

import java.util.List;

import entity.LearnRecord;

/**
 * Data access interface for ViewHistory use case.
 * Follows the same pattern as RankUserDataAccessInterface.
 */
public interface ViewHistoryUserDataAccessInterface {
    /**
     * Returns learning records for a specific user.
     * @param username the username to get records for
     * @return list of learning records for the user
     */
    List<LearnRecord> get(String username);

}
