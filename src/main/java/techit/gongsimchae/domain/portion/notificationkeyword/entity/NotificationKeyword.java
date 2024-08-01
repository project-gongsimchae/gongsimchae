package techit.gongsimchae.domain.portion.notificationkeyword.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.portion.notifications.entity.Notifications;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "notification_keyword")
public class NotificationKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notifications_id")
    private Notifications notifications;

    @Builder
    public NotificationKeyword(String keyword, LocalDateTime createDate, User user, Notifications notifications) {
        this.keyword = keyword;
        this.createDate = createDate;
        this.user = user;
        this.notifications = notifications;
    }
}
