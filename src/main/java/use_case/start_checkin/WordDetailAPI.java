package use_case.start_checkin;

public interface WordDetailAPI {
    /**
     * Retrieves an example sentence or usage for the specified word.
     *
     * @param text the word to retrieve an example for
     * @return an example sentence or usage of the provided word
     */
    String getWordExample(String text);
}
