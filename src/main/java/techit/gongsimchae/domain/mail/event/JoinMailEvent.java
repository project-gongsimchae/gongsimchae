package techit.gongsimchae.domain.mail.event;

import lombok.Getter;
import lombok.ToString;
import techit.gongsimchae.domain.common.user.dto.UserJoinReqDtoWeb;

@Getter
@ToString
public class JoinMailEvent {
    private String email;

    public JoinMailEvent(UserJoinReqDtoWeb joinReqDtoWeb) {
        this.email = joinReqDtoWeb.getEmail();
    }
}
