package techit.gongsimchae.domain.portion.notificationkeyword.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.portion.notifications.entity.Notifications;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "notification_keyword")
public class NotificationKeyword extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public NotificationKeyword(String keyword, User user) {
        this.keyword = keyword;
        this.user = user;
    }
}
