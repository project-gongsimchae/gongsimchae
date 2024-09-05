package techit.gongsimchae.domain.groupbuying.item.dto;

import java.time.LocalDateTime;
import lombok.Data;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;

@Data
public class ReviewedItemResDtoWeb {

    private String itemName;
    private LocalDateTime reviewAbleDate;
    private String thumbnailUrl;
    private String UID;
    private Long orderItemId;

    public ReviewedItemResDtoWeb(Item item, String thumbnailUrl, LocalDateTime createDate, OrderItem orderItem){
        this.itemName = item.getName();
        this.reviewAbleDate = createDate;
        this.thumbnailUrl = thumbnailUrl;
        this.UID = item.getUID();
        this.orderItemId = orderItem.getId();
    }
}
