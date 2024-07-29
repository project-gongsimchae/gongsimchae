package techit.gongsimchae.global.util;

import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
@Slf4j
public class AuthCode {
    public static String createSixCode(){

        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                int randInt = random.nextInt(10);
                builder.append(randInt);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("SixCode exception {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
