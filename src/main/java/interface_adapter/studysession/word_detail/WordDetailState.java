package interface_adapter.studysession.word_detail;

public class WordDetailState {

    private String translation = "";
    private String username = "";
    private String example = "";

    public WordDetailState(String translation,
                           String username,
                           String example) {
        this.translation = translation;
        this.username = username;
        this.example = example;
    }

    public WordDetailState() {}

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
