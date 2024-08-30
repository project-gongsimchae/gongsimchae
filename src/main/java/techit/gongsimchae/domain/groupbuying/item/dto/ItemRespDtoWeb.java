package techit.gongsimchae.domain.groupbuying.item.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.item.entity.ItemStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRespDtoWeb {
    private Long id;
    private String name;
    private String intro;
    private Integer originalPrice;
    private Integer discountRate;
    private Integer pointAccumulationRate;
    private Integer groupBuyingQuantity;
    private LocalDateTime groupBuyingLimitTime;
    private Boolean deleteStatus;
    private String UID;
    private Long cumulativeSalesVolume;
    private Long reviewCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private List<String> imageUrls = new ArrayList<>();
    private ItemStatus itemStatus;

    public ItemRespDtoWeb(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.intro = item.getIntro();
        this.originalPrice = item.getOriginalPrice();
        this.discountRate = item.getDiscountRate();
        this.pointAccumulationRate = item.getPointAccumulationRate();
        this.groupBuyingQuantity = item.getGroupBuyingQuantity();
        this.groupBuyingLimitTime = item.getGroupBuyingLimitTime();
        this.deleteStatus = item.getDeleteStatus();
        this.UID = item.getUID();
        this.cumulativeSalesVolume = item.getCumulativeSalesVolume();
        this.reviewCount = item.getReviewCount();
        this.createDate = item.getCreateDate();
        this.updateDate = item.getUpdateDate();
        // todo 추후 이미지파일 넣어야됨
        this.imageUrls = null;
        this.itemStatus = item.getItemStatus();
    }

    public ItemRespDtoWeb(Item item, List<String> imageUrls) {
        this.id = item.getId();
        this.name = item.getName();
        this.intro = item.getIntro();
        this.originalPrice = item.getOriginalPrice();
        this.discountRate = item.getDiscountRate();
        this.pointAccumulationRate = item.getPointAccumulationRate();
        this.groupBuyingQuantity = item.getGroupBuyingQuantity();
        this.groupBuyingLimitTime = item.getGroupBuyingLimitTime();
        this.deleteStatus = item.getDeleteStatus();
        this.UID = item.getUID();
        this.cumulativeSalesVolume = item.getCumulativeSalesVolume();
        this.reviewCount = item.getReviewCount();
        this.createDate = item.getCreateDate();
        this.updateDate = item.getUpdateDate();
        this.imageUrls = imageUrls;
        this.itemStatus = item.getItemStatus();
    }
}
