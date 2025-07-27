package use_case.rank;

import entity.LearnRecord;

import java.util.List;
import java.util.Map;

public interface RankUserDataAccessInterface {
    /**
     * Returns a map of all usernames and their scores.
     */
    Map<String, List<LearnRecord>> getAllUsers();
}
