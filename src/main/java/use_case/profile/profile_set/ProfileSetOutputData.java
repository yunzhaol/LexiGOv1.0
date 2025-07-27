package use_case.profile.profile_set;

import entity.Language;

public class ProfileSetOutputData {
    private final String username;
    private final Language newlanguage;

    public ProfileSetOutputData(String username, Language newlanguage) {
        this.username = username;
        this.newlanguage = newlanguage;
    }

    public Language getNewlanguage() {
        return newlanguage;
    }

    public String getUsername() {
        return username;
    }
}
