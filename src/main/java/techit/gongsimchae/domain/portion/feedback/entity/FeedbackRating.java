package techit.gongsimchae.domain.portion.feedback.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FeedbackRating {

    NEGATIVE("별로에요"),POSITIVE("좋아요"),EXCELLENT("최고에요");

    String description;
}
