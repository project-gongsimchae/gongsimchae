package techit.gongsimchae.global.exception;

import org.springframework.security.core.AuthenticationException;

public class CustomTokenException extends AuthenticationException {
    public CustomTokenException(String message) {
        super(message);
    }
}
