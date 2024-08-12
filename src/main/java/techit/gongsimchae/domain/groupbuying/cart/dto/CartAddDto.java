package techit.gongsimchae.domain.groupbuying.cart.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CartAddDto {
    private Long userId;
    private Long itemId;
    private int quantity;

    @Builder
    public CartAddDto(Long userId, Long itemId, int quantity) {
        this.userId = userId;
        this.itemId = itemId;
        this.quantity = quantity;
    }
}
