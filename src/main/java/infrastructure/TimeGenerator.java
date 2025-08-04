package infrastructure;

import java.time.LocalDateTime;

import use_case.finish_checkin.TimeGetter;

public class TimeGenerator implements TimeGetter {

    @Override
    public LocalDateTime generate() {
        return LocalDateTime.now();
    }
}
