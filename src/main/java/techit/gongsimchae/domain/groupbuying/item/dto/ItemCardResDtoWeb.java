package techit.gongsimchae.domain.groupbuying.item.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;

@Data
@NoArgsConstructor
public class ItemCardResDtoWeb {

    private Long id;

    private String name;
    private String intro;
    private Integer originalPrice;
    private Integer discountRate;
    private Integer pointAccumulationRate;
    private Integer groupBuyingQuantity;
    private LocalDateTime groupBuyingLimitTime;
    private String UID;
    private Long cumulativeSalesVolume;
    private Long reviewCount;
    private String itemBannerImage;

    public ItemCardResDtoWeb(Item item, ImageFile imageFile){
        this.id = item.getId();
        this.name = item.getName();
        this.intro = item.getIntro();
        this.originalPrice = item.getOriginalPrice();
        this.discountRate = item.getDiscountRate();
        this.pointAccumulationRate = item.getPointAccumulationRate();
        this.groupBuyingQuantity = item.getGroupBuyingQuantity();
        this.groupBuyingLimitTime = item.getGroupBuyingLimitTime();
        this.UID = item.getUID();
        this.cumulativeSalesVolume = item.getCumulativeSalesVolume();
        this.reviewCount = item.getReviewCount();
        this.itemBannerImage = imageFile.getStoreFilename();
    }

}
