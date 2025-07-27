package use_case.studysession.word_detail;

public class WordDetailOutputData {

    private String translation;
    private String example;

    public WordDetailOutputData(String translation,
                                String example) {
        this.translation = translation;
        this.example = example;
    }

    public String getTranslation() {
        return translation;
    }

    public String getExample() {
        return example;
    }
}
