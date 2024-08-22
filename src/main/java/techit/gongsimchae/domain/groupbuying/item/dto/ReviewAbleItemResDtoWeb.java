package techit.gongsimchae.domain.groupbuying.item.dto;

import java.time.LocalDateTime;
import lombok.Data;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;

@Data
public class ReviewAbleItemResDtoWeb {

    private String itemName;
    private LocalDateTime reviewAbleDate;
    private String thumbnailUrl;
    private String UID;

    public ReviewAbleItemResDtoWeb(Item item, String thumbnailUrl){
        this.itemName = item.getName();
        this.reviewAbleDate = LocalDateTime.now();
        this.thumbnailUrl = thumbnailUrl;
        this.UID = item.getUID();
    }


}