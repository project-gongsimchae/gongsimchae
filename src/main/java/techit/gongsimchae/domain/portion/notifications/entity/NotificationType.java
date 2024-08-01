package techit.gongsimchae.domain.portion.notifications.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    CHAT("채팅"), REVIEW("리뷰"), KEYWORD("키워드");

    private String value;
}
