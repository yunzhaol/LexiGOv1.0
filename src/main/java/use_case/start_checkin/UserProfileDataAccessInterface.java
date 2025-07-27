package use_case.start_checkin;

import entity.Language;

public interface UserProfileDataAccessInterface {
    Language getLanguage(String username);

}
