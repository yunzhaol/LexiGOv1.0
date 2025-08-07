package use_case.start_checkin;

public interface FormatDetector {
    /**
     * Executes the format detection on the provided input string.
     *
     * @param length the string to be evaluated against the detector's format
     * @return {@code true} if the input matches the expected format;
     *         {@code false} otherwise
     */
    boolean execute(String length);
}
