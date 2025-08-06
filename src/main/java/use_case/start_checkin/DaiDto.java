package use_case.start_checkin;

import use_case.gateway.UserRecordDataAccessInterface;

public class DaiDto {
    private final UserRecordDataAccessInterface userDataAccessObject;
    private final WordBookAccessInterface wordBookAccessObject;
    private final UserCheckInDeckAccessInterface userDeckAccessObject;
    private final WordDataAccessInterface wordDataAccessObject;
    private final UserProfileDataAccessInterface userProfileDataAccessObject;

    public DaiDto(UserRecordDataAccessInterface userDataAccessObject,
                  WordBookAccessInterface wordBookAccessObject,
                  UserCheckInDeckAccessInterface userDeckAccessObject,
                  WordDataAccessInterface wordDataAccessObject,
                  UserProfileDataAccessInterface userProfileDataAccessObject) {
        this.userDataAccessObject = userDataAccessObject;
        this.wordBookAccessObject = wordBookAccessObject;
        this.userDeckAccessObject = userDeckAccessObject;
        this.wordDataAccessObject = wordDataAccessObject;
        this.userProfileDataAccessObject = userProfileDataAccessObject;
    }

    public UserRecordDataAccessInterface getUserDataAccessObject() {
        return userDataAccessObject;
    }

    public WordBookAccessInterface getWordBookAccessObject() {
        return wordBookAccessObject;
    }

    public UserCheckInDeckAccessInterface getUserDeckAccessObject() {
        return userDeckAccessObject;
    }

    public WordDataAccessInterface getWordDataAccessObject() {
        return wordDataAccessObject;
    }

    public UserProfileDataAccessInterface getUserProfileDataAccessObject() {
        return userProfileDataAccessObject;
    }
}
