package techit.gongsimchae.domain.portion.notifications.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.common.user.entity.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KeywordNotiEvent {
    private User user;
    private String keyword;
    private String url;
    private String address;
    private String title;
}
