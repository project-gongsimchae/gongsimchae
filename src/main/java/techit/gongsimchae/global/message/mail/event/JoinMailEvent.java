package techit.gongsimchae.global.message.mail.event;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class JoinMailEvent {
    private String email;

    public JoinMailEvent(String email) {
        this.email = email;
    }
}
