package use_case.gateway;

import entity.LearnRecord;

import java.util.List;

public interface UserRecordDataAccessInterface {
    List<LearnRecord> get(String username);
}
