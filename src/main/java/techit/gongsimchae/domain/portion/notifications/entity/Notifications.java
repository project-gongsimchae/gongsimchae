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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Notifications(User user, String content, NotificationType notificationType) {
        this.content = content;
        this.isRead = 0;
        this.user = user;
        this.notificationType = notificationType;
    }

    public void read(){
        this.isRead = 1;
    }
}
