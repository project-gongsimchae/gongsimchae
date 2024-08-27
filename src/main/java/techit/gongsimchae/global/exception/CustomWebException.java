package techit.gongsimchae.global.exception;

public class CustomWebException extends RuntimeException{
    public CustomWebException(String message) {
        super(message);
    }

    public CustomWebException(Throwable cause) {
        super(cause);
    }
}
