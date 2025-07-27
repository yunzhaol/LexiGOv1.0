package use_case.start_checkin;

import entity.Language;

public interface WordTranslationAPI {
    String getTranslation(String text, Language targetLang);

}
