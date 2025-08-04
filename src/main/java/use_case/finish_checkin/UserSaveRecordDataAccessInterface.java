package use_case.finish_checkin;

import entity.LearnRecord;

public interface UserSaveRecordDataAccessInterface {
    /**
     * Saves a completed learning session record.
     *
     * @param learnRecord the {@link LearnRecord} representing the session to persist;
     *                    must not be {@code null}
     */
    void save(LearnRecord learnRecord);
}
