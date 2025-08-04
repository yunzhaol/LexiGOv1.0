package use_case.rank;

import java.util.List;
import java.util.Map;

import entity.LearnRecord;

public interface RankUserDataAccessInterface {
    /**
     * Retrieves a map containing usernames and their corresponding to learn records.
     *
     * @return a map where each key is a username, and each value is a list of LearnRecord objects
     */
    Map<String, List<LearnRecord>> getAllUsers();
}
