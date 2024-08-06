package techit.gongsimchae.domain.groupbuying.item.dto;

import lombok.Data;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
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
    private List<ImageFile> imageFiles = new ArrayList<>();

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
        this.imageFiles = null;
    }
}
