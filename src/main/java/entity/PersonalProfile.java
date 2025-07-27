package entity;

public class PersonalProfile {

    private String username;
    private Language language;

    public PersonalProfile(String username, Language language) {
        this.username = username;
        this.language = language;
    }

    public Language getLanguage() {
        return language;
    }

    public String getUsername() {
        return username;
    }

}
