package use_case.profile;

import entity.Language;

public class ProfileOutputData {
    private String username;
    private Language language;
    private Language[] languages;

    public ProfileOutputData(String username, Language language, Language[] languages) {
        this.username = username;
        this.language = language;
        this.languages = languages;
    }

    public Language[] getLanguages() {
        return languages;
    }

    public Language getLanguage() {
        return language;
    }

    public String getUsername() {
        return username;
    }
}
