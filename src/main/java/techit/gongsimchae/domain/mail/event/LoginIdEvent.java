package techit.gongsimchae.domain.mail.event;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginIdEvent {
    private String email;
    private String loginId;

    public LoginIdEvent(String email, String loginId) {
        this.email = email;
        this.loginId = loginId;
    }
}
