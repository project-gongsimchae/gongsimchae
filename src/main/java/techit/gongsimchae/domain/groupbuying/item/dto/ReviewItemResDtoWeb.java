package techit.gongsimchae.domain.groupbuying.item.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewItemResDtoWeb {
    private List<ReviewAbleItemResDtoWeb> reviewAbleItemResDtoWebs;
    private List<ReviewedItemResDtoWeb> reviewedItemResDtoWebs;

    public ReviewItemResDtoWeb(List<ReviewAbleItemResDtoWeb> reviewAbleItemResDtoWeb, List<ReviewedItemResDtoWeb> reviewedItemResDtoWeb){
        this.reviewAbleItemResDtoWebs = reviewAbleItemResDtoWeb;
        this.reviewedItemResDtoWebs = reviewedItemResDtoWeb;
    }
}
