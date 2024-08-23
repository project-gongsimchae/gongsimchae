package techit.gongsimchae.domain.common.user.mail.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PasswordEvent {
    private String email;
    private String password;

    public PasswordEvent(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
