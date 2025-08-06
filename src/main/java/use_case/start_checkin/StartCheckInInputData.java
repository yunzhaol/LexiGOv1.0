package use_case.start_checkin;

public final class StartCheckInInputData {
    private String username;
    private String length;

    public StartCheckInInputData(String username, String length) {
        this.username = username;
        this.length = length;
    }

    public String getUsername() {
        return username;
    }

    public String getLength() {
        return length;
    }

}
