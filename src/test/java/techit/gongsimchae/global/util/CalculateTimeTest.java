package techit.gongsimchae.global.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculateTimeTest {

    @Test
    void 시간구하기_test() throws Exception {
        // given
        long secondsUntilEndOfDay = CalculateTime.getSecondsUntilEndOfDay();
        long secondsUntilEndOfMonth = CalculateTime.getSecondsUntilEndOfMonth();
        long secondsUntilEndOfWeek = CalculateTime.getSecondsUntilEndOfWeek();


        // when

        // then
        System.out.println("secondsUntilEndOfWeek = " + secondsUntilEndOfWeek);
        System.out.println("secondsUntilEndOfMonth = " + secondsUntilEndOfMonth);
        System.out.println("secondsUntilEndOfDay = " + secondsUntilEndOfDay);
    }

}