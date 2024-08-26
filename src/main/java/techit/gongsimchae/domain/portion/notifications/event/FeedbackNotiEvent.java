package techit.gongsimchae.domain.portion.notifications.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import techit.gongsimchae.domain.common.user.entity.User;

@Data
@AllArgsConstructor
public class FeedbackNotiEvent {
    private User user;
    private String title;
    private String url;

}
