package entity;

/**
 * Supported source and target languages in the system.
 *
 * <p>Each constant stores:</p>
 * <ul>
 *   <li><strong>code</strong> – the two-letter ISO-639-1 language code</li>
 *   <li><strong>displayName</strong> – an English human-readable name</li>
 * </ul>
 */
public enum Language {

    EN("en", "English"),
    ZH("zh", "Chinese"),
    JP("ja", "Japanese"),
    FR("fr", "French"),
    DE("de", "German"),
    ES("es", "Spanish");

    private final String code;
    private final String displayName;

    Language(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * Returns the ISO-639-1 two-letter language code.
     *
     * @return the code, e.g. {@code "en"}
     */
    public String code() {
        return code;
    }

    /**
     * Returns the English display name of the language.
     *
     * @return the display name, e.g. {@code "English"}
     */
    public String displayName() {
        return displayName;
    }
}
