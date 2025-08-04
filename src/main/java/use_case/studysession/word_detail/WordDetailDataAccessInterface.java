package use_case.studysession.word_detail;

public interface WordDetailDataAccessInterface {
    /**
     * Retrieves the translation for the word at the specified index.
     *
     * @param input the index of the word
     * @return the translation of the specified word
     */
    String getTranslation(int input);

    /**
     * Retrieves an example usage for the word at the specified index.
     *
     * @param input the index of the word
     * @return an example sentence using the specified word
     */
    String getExample(int input);
}
