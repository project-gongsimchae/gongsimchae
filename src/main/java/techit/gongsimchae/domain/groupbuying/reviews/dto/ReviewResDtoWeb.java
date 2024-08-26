package techit.gongsimchae.domain.groupbuying.reviews.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.groupbuying.reviews.entity.Review;
import techit.gongsimchae.domain.groupbuying.reviews.entity.SecretStatus;

@Data
@NoArgsConstructor
public class ReviewResDtoWeb {

    private String title;
    private int starPoint;
    private String content;
    private Boolean secretStatus;
    private String images;

    public ReviewResDtoWeb(Review review, String images){
        this.title = review.getTitle();
        this.starPoint = review.getStarPoint();
        this.content = review.getContent();
        this.secretStatus = review.getSecretStatus().equals(SecretStatus.SECRET);
        this.images = images;
    }
}
