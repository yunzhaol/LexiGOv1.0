package use_case.finish_checkin;

import java.time.LocalDateTime;

public interface TimeGetter {
    /**
     * Generates the current timestamp.
     *
     * @return the current {@link java.time.LocalDateTime}; never {@code null}
     */
    LocalDateTime generate();
}
