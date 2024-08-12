package techit.gongsimchae.domain.portion.notifications.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    INQUIRY("문의"), CHATROOM("채팅방"), KEYWORD("알림 키워드"), CHAT("채팅");
    private String description;
}