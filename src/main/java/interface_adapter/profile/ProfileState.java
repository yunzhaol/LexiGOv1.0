package interface_adapter.profile;

import entity.Language;

/**
 * The state for the Login View Model.
 */
public class ProfileState {
    private String username = "";
    private String languageError;
    private Language oldlanguage;
    private Language[] languages;

    public ProfileState(String username, String languageError, Language oldlanguage, Language[] languages) {
        this.username = username;
        this.languageError = languageError;
        this.oldlanguage = oldlanguage;
        this.languages = languages;
    }

    public ProfileState() {
    }

    public Language[] getLanguages() {
        return languages;
    }

    public void setLanguages(Language[] languages) {
        this.languages = languages;
    }

    public String getUsername() {
        return username;
    }

    public String getLanguageError() {
        return languageError;
    }

    public void setLanguageError(String languageError) {
        this.languageError = languageError;
    }

    public Language getOldlanguage() {
        return oldlanguage;
    }

    public void setOldlanguage(Language oldlanguage) {
        this.oldlanguage = oldlanguage;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
