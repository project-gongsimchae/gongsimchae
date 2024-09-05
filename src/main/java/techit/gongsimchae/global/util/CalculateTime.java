package techit.gongsimchae.global.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class CalculateTime {

    public static long getSecondsUntilEndOfDay(){
        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        return todayEndSecond - currentSecond;

    }

    public static long getNowSeconds(){
        return Instant.now().getEpochSecond();
    }

    public static long getSecondsInAWeek() {
        // 1주일 = 7일, 1일 = 24시간, 1시간 = 60분, 1분 = 60초
        return getNowSeconds() - 7L * 24 * 60 * 60;

    }


    public static long getSecondsInAMonth() {
        // 1달 = 30일, 1일 = 24시간, 1시간 = 60분, 1분 = 60초
        return getNowSeconds() - 30L * 24 * 60 * 60;
    }

    public static String getToday() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static String getWeek() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyww"));
    }
}
