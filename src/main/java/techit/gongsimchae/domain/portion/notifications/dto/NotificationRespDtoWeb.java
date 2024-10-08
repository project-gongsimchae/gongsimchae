package techit.gongsimchae.domain.portion.notifications.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.portion.notifications.entity.NotificationType;
import techit.gongsimchae.domain.portion.notifications.entity.Notifications;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRespDtoWeb {

    private Long id;

    private String content;

    private Integer isRead; // 0 안읽음 1 읽음
    private NotificationType notificationType;
    private Long unreadCount;
    private String url;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public NotificationRespDtoWeb(Notifications notifications) {
        this.id = notifications.getId();
        this.content = notifications.getContent();
        this.isRead = notifications.getIsRead();
        this.notificationType = notifications.getNotificationType();
        this.url = notifications.getUrl();
        this.createDate = notifications.getCreateDate();
        this.updateDate = notifications.getUpdateDate();
    }
}
