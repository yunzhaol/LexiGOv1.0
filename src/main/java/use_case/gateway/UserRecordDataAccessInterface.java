package use_case.gateway;

import java.util.List;

import entity.LearnRecord;

public interface UserRecordDataAccessInterface {
    /**
     * Fetches all {@link LearnRecord} instances associated with the given user.
     *
     * @param username the username whose history to retrieve; must not be {@code null}
     * @return a {@link java.util.List} of {@link LearnRecord}; never {@code null}
     */
    List<LearnRecord> get(String username);
}
