package techit.gongsimchae.domain.portion.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.portion.feedback.entity.FeedbackRating;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackReqDtoWeb {

    private String url;
    private FeedbackRating feedbackRating;
    private String nickname;
    private Long userId;
}
