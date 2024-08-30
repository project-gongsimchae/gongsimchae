package techit.gongsimchae.domain.portion.notifications.event;

import lombok.Data;
import techit.gongsimchae.domain.common.user.entity.User;

@Data
public class GroupBuyingNotiEvent {
    private User user;
    private String content;

    public GroupBuyingNotiEvent(User user, String content) {
        this.user = user;
        this.content = content;
    }
}
