package techit.gongsimchae.domain.groupbuying.item.dto;

import java.time.LocalDateTime;
import lombok.Data;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;

@Data
public class ReviewedItemResDtoWeb {

    private String itemName;
    private LocalDateTime reviewAbleDate;
    private String thumbnailUrl;
    private String UID;

    public ReviewedItemResDtoWeb(Item item, String thumbnailUrl, LocalDateTime createDate){
        this.itemName = item.getName();
        this.reviewAbleDate = createDate;
        this.thumbnailUrl = thumbnailUrl;
        this.UID = item.getUID();
    }
}