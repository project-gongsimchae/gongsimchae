package techit.gongsimchae.domain.groupbuying.item.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemCreateDto;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemUpdateDto;
import techit.gongsimchae.domain.groupbuying.category.entity.Category;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Item (ItemCreateDto itemCreateDto, Category category) {
        this.name = itemCreateDto.getName();
        this.intro = itemCreateDto.getIntro();
        this.originalPrice = itemCreateDto.getOriginalPrice();
        this.discountRate = itemCreateDto.getDiscountRate();
        this.pointAccumulationRate = itemCreateDto.getPointAccumulationRate();
        this.groupBuyingQuantity = itemCreateDto.getGroupBuyingQuantity();
        this.groupBuyingLimitTime = itemCreateDto.getGroupBuyingLimitTime();
        this.category = category;
        this.UID = UUID.randomUUID().toString().substring(0,8);
        this.cumulativeSalesVolume = 0L;
        this.reviewCount = 0L;
    }

    public void UpdateDto (ItemUpdateDto itemUpdateDto, Category category) {
        this.name = itemUpdateDto.getName();
        this.intro = itemUpdateDto.getIntro();
        this.originalPrice = itemUpdateDto.getOriginalPrice();
        this.discountRate = itemUpdateDto.getDiscountRate();
        this.pointAccumulationRate = itemUpdateDto.getPointAccumulationRate();
        this.groupBuyingQuantity = itemUpdateDto.getGroupBuyingQuantity();
        this.groupBuyingLimitTime = itemUpdateDto.getGroupBuyingLimitTime();
        this.category = category;
    }
}
