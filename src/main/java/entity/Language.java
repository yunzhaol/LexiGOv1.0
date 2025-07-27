package entity;

/**
 * Supported source / target languages in the system.
 * <p>
 * code —— ISO‑639‑1 two‑letter code
 * displayName —— human‑readable name (English)
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
        this.code        = code;
        this.displayName = displayName;
    }

    /** ISO‑639‑1 two‑letter code, e.g. "en" */
    public String code() {
        return code;
    }

    /** English display name, e.g. "English" */
    public String displayName() {
        return displayName;
    }

    /**
     * Lookup by ISO code (case‑insensitive).
     * Returns null if not found.
     */
    public static Language fromCode(String code) {
        if (code == null) return null;
        for (Language lang : values()) {
            if (lang.code.equalsIgnoreCase(code)) {
                return lang;
            }
        }
        return null;
    }
}
