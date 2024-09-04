//package techit.gongsimchae.common;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.security.SecureRandom;
//
//public class RandomCode {
//
//
//    @Test
//    @DisplayName("인증번호 6자리 나오는지 확인")
//    void AuthCode_6_test() throws Exception {
//        // given
//        SecureRandom random = SecureRandom.getInstanceStrong();
//        StringBuilder builder = new StringBuilder();
//        int i = random.nextInt(10);
//        System.out.println("i = " + i);
//
//        // when
//        for (int j = 0; j < 6; j++) {
//            builder.append(random.nextInt(10));
//        }
//        System.out.println("builder = " + builder);
//
//        // then
//    }
//}
