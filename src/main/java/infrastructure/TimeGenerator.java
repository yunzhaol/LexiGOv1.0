package infrastructure;

import use_case.finish_checkin.TimeGetter;

import java.time.LocalDateTime;

public class TimeGenerator implements TimeGetter {

    @Override
    public LocalDateTime generate() {
        return LocalDateTime.now();
    }
}
