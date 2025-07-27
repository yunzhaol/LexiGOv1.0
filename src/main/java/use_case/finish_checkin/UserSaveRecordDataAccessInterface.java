package use_case.finish_checkin;

import entity.LearnRecord;

public interface UserSaveRecordDataAccessInterface {
    void save(LearnRecord learnRecord);
}
