package techit.gongsimchae.domain.portion.notifications.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    INQUIRY("문의"), KEYWORD("알림 키워드"), CHAT("채팅"), FEEDBACK("피드백"), GROUP_BUYING("공동구매");
    private String description;
}
