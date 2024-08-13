package techit.gongsimchae.domain.groupbuying.cart.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CartItemDto {
    private Long cartId;
    private Long itemId;
    private String itemName;
    private Integer originalPrice;
    private Integer discountRate;
    private Integer discountPrice;
    private Integer groupBuyingQuantity;
    private Integer quantity;
    private Integer totalPrice;
    private LocalDateTime groupBuyingLimitTime;

    @Builder
    public CartItemDto(Long cartId, Long itemId, String itemName, Integer originalPrice, Integer discountRate, Integer discountPrice, Integer groupBuyingQuantity, Integer quantity, Integer totalPrice, LocalDateTime groupBuyingLimitTime) {
        this.cartId = cartId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.originalPrice = originalPrice;
        this.discountRate = discountRate;
        this.discountPrice = discountPrice;
        this.groupBuyingQuantity = groupBuyingQuantity;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.groupBuyingLimitTime = groupBuyingLimitTime;
    }
}
