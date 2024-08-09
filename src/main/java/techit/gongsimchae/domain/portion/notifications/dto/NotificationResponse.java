package techit.gongsimchae.domain.portion.notifications.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.portion.notifications.entity.NotificationType;
import techit.gongsimchae.domain.portion.notifications.entity.Notifications;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NotificationResponse {
    private String content;
    private NotificationType notificationType;
    private String url;

    private List<NotificationRespDtoWeb> notifications = new ArrayList<>();
    private Long unreadCount;

    public NotificationResponse(List<NotificationRespDtoWeb> notifications, Long unreadCount) {
        this.notifications = notifications;
        this.unreadCount = unreadCount;
    }

    public NotificationResponse(Notifications notifications) {
        this.content = notifications.getContent();
        this.notificationType = notifications.getNotificationType();
        this.url = notifications.getUrl();
    }

    public NotificationResponse(String content) {
        this.content = content;
    }
}
