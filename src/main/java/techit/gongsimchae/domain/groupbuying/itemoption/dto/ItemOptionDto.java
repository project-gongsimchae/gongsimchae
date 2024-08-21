package techit.gongsimchae.domain.groupbuying.itemoption.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ItemOptionDto {
    private Long itemId;
    private Long itemOptionId;
    private String content;
    private String optionsInfo;
    private String itemName;
    private String itemUID;
    private Integer optionPrice;
    private Integer quantity;
    private Integer totalPrice;
    private Integer originalPrice;
    private Integer discountRate;
    private Integer discountPrice;


    @Builder
    public ItemOptionDto(Long itemId, Long itemOptionId, String content, String optionsInfo, String itemName, Integer optionPrice, Integer quantity, Integer totalPrice, Integer originalPrice, Integer discountRate, Integer discountPrice, String itemUID) {
        this.itemId = itemId;
        this.itemOptionId = itemOptionId;
        this.content = content;
        this.optionsInfo = optionsInfo;
        this.itemName = itemName;
        this.optionPrice = optionPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.originalPrice = originalPrice;
        this.discountRate = discountRate;
        this.discountPrice = discountPrice;
        this.itemUID = itemUID;
    }
}
