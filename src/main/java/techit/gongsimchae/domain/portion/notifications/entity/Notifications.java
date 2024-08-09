package techit.gongsimchae.domain.portion.notifications.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.user.entity.User;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notifications extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Integer isRead; // 0 안읽음 1 읽음

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Builder
    public Notifications(String content, Integer isRead, NotificationType notificationType, String url, User user) {
        this.content = content;
        this.isRead = isRead;
        this.notificationType = notificationType;
        this.url = url;
        this.user = user;
    }

    public void read(){
        this.isRead = 1;
    }
}
