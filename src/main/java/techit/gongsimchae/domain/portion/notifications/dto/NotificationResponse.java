package techit.gongsimchae.domain.portion.notifications.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NotificationResponse {
    private List<NotificationRespDtoWeb> notifications = new ArrayList<>();
    private Long unreadCount;

    public NotificationResponse(List<NotificationRespDtoWeb> notifications, Long unreadCount) {
        this.notifications = notifications;
        this.unreadCount = unreadCount;
    }
}
