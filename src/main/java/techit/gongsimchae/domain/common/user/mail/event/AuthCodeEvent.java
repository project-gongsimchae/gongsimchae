package techit.gongsimchae.domain.common.user.mail.event;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AuthCodeEvent {
    private String email;
    private String code;

    public AuthCodeEvent(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
