package techit.gongsimchae.domain.groupbuying.reviews.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.groupbuying.reviews.entity.Reviews;
import techit.gongsimchae.domain.groupbuying.reviews.entity.SecretStatus;

@Data
@NoArgsConstructor
public class ReviewsResDtoWeb {

    private String title;
    private int starPoint;
    private String content;
    private Boolean secretStatus;
    private String images;

    public ReviewsResDtoWeb(Reviews reviews, String images){
        this.title = reviews.getTitle();
        this.starPoint = reviews.getStarPoint();
        this.content = reviews.getContent();
        this.secretStatus = reviews.getSecretStatus().equals(SecretStatus.SECRET);
        this.images = images;
    }
}
