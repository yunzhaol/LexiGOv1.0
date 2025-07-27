package entity;

public class PersonalProfileFactory implements ProfileFactory {
    @Override
    public PersonalProfile createPersonalProfile(String username, Language language) {
        return new PersonalProfile(username, language);
    }
}
