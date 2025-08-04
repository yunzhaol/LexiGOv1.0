package use_case.start_checkin;

import entity.Language;

public interface UserProfileDataAccessInterface {
    /**
     * Obtains the preferred language for a given user.
     *
     * @param username the username whose language preference is being retrieved
     * @return the Language chosen by the user
     */
    Language getLanguage(String username);
}
