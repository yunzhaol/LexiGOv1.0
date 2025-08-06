package use_case.profile.profile_set;

import entity.Language;

public final class ProfileSetInputData {
    private final String username;
    private final Language newlanguage;
    private final Language oldlanguage;

    public ProfileSetInputData(String username, Language oldlanguage, Language newlanguage) {
        this.username = username;
        this.newlanguage = newlanguage;
        this.oldlanguage = oldlanguage;
    }

    public Language getNewlanguage() {
        return newlanguage;
    }

    public Language getOldlanguage() {
        return oldlanguage;
    }

    public String getUsername() {
        return username;
    }
}
