package techit.gongsimchae.domain.portion.notifications.event;

import lombok.Data;
import techit.gongsimchae.domain.common.user.entity.User;

@Data
public class ChatNotiEvent {
    private User user;
    private String url;

    public ChatNotiEvent(User user, String url) {
        this.user = user;
        this.url = url;
    }
}
