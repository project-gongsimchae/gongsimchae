package techit.gongsimchae.domain.groupbuying.cart.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartAddRequest {
    private List<CartItemRequest> cartItems;

    @Data
    public static class CartItemRequest{
        private Long itemOptionId;
        private int quantity;
    }
}
