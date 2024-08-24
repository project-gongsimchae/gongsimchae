package techit.gongsimchae.global.util;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class CalculateTime {

    public static long getSecondsUntilEndOfDay(){
        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        return todayEndSecond - currentSecond;

    }


    public static long getSecondsUntilEndOfWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX);
        return ChronoUnit.SECONDS.between(now, endOfWeek);
    }

    /**
     * 현재 시점부터 이번 달의 종료까지 남은 초를 계산합니다.
     *
     */
    public static long getSecondsUntilEndOfMonth() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        return ChronoUnit.SECONDS.between(now, endOfMonth);
    }
}
