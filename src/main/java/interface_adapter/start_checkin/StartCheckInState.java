package interface_adapter.start_checkin;

public class StartCheckInState {

    private String numberWords = "";
    private String InputNumberError;
    private String username;


    @Override
    public String toString() {
        return "StartCheckInState{"
                + "Username='" + username + '\''
                + "numberWords='"        + numberWords        + '\''
                + ", negativeNumberError='" + InputNumberError + '\''
                + '}';
    }

    public String getNumberWords() {
        return numberWords;
    }

    public String getInputNumberError() {
        return InputNumberError;
    }

    public void setNumberWords(String numberWords) {
        this.numberWords = numberWords;
    }

    public void setInputNumberError(String inputNumberError) {
        this.InputNumberError = inputNumberError;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
