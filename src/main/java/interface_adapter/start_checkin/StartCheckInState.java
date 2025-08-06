package interface_adapter.start_checkin;

public final class StartCheckInState {

    private String numberWords = "";
    private String inputNumberError;
    private String username;

    @Override
    public String toString() {
        return "StartCheckInState{"
                + "Username='" + username + '\''
                + "numberWords='" + numberWords + '\''
                + ", negativeNumberError='" + inputNumberError + '\''
                + '}';
    }

    public String getNumberWords() {
        return numberWords;
    }

    public String getInputNumberError() {
        return inputNumberError;
    }

    public void setNumberWords(String numberWords) {
        this.numberWords = numberWords;
    }

    public void setInputNumberError(String inputNumberError) {
        this.inputNumberError = inputNumberError;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
