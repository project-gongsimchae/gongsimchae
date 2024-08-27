package techit.gongsimchae.domain.portion.notifications.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import techit.gongsimchae.domain.common.user.entity.User;

import java.util.List;

@Data
@AllArgsConstructor
public class FeedbackNotiEvent {
    private List<User> users;
    private String title;

}
