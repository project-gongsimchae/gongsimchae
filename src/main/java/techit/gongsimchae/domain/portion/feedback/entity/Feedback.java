package techit.gongsimchae.domain.portion.feedback.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.portion.feedback.dto.FeedbackReqDtoWeb;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class Feedback extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FeedbackRating feedbackRating;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Feedback(FeedbackReqDtoWeb dtoWeb, User user) {
        this.feedbackRating = dtoWeb.getFeedbackRating();
        this.url = dtoWeb.getUrl();
        this.user = user;
    }
}
