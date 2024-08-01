package techit.gongsimchae.domain.portion.notifications.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import techit.gongsimchae.domain.common.user.entity.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadStatus readStatus;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Notifications(NotificationType notificationType, ReadStatus readStatus, LocalDateTime createDate, User user) {
        this.notificationType = notificationType;
        this.readStatus = readStatus;
        this.createDate = createDate;
        this.user = user;
    }
}
