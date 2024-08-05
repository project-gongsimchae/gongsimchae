package techit.gongsimchae.domain.portion.chatroom.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "chat_room")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subdivision_id")
    private Subdivision subdivision;

    @Builder
    public ChatRoom(String message, LocalDateTime createDate, User user, Subdivision subdivision) {
        this.message = message;
        this.createDate = createDate;
        this.user = user;
        this.subdivision = subdivision;
    }
}
