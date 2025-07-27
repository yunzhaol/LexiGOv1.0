package use_case.profile;

import entity.Language;

public interface ProfileUserDataAccessInterface {
    Language getLanguage(String username);
}
