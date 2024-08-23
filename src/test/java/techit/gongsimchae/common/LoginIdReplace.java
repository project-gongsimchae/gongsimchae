package techit.gongsimchae.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoginIdReplace {

    @Test
    @DisplayName("아이디를 *로 바꾸는 작업")
    void loginId_Replace_test() throws Exception {
        // given
        String loginId = "gongsimchae";
        String loginId2 = "adlkfaslkdfjsalk";
        StringBuilder builder = new StringBuilder("gongsimchae");
        StringBuilder stringBuilder = new StringBuilder(loginId2);

        System.out.println("loginId = " + loginId.length());
        System.out.println("loginId2 = " + loginId2.length());
        StringBuilder replace = new StringBuilder();
        StringBuilder replace2 = new StringBuilder();
        // when
        if (loginId.length() % 2 == 1) {
            replace = builder.replace(loginId.length() / 2, loginId.length(), "*".repeat(loginId.length() / 2 + 1));
        }
         if(loginId2.length() % 2 == 0) {
             replace2 = stringBuilder.replace(loginId2.length() / 2, loginId2.length(), "*".repeat(loginId2.length() / 2));
         }



        System.out.println("replace = " + replace);
        System.out.println("replace2 = " + replace2);
        // then
        Assertions.assertThat(replace.length()).isEqualTo(loginId.length());
        Assertions.assertThat(replace2.length()).isEqualTo(loginId2.length());
    }
}
