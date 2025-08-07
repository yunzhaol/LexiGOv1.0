package interface_adapter.start_checkin;

public final class StartCheckInState {

    private String numberWords = "";
    private String inputNumberError;
    private String username;

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
