package use_case.start_checkin;

import entity.Language;

public interface WordTranslationAPI {
    /**
     * Translates the given text into the target language.
     *
     * @param text       the text to be translated
     * @param targetLang the language into which the text should be translated
     * @return the translated text in the target language
     */
    String getTranslation(String text, Language targetLang);
}
