package techit.gongsimchae.domain.groupbuying.cart.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CartPriceInfoDto {
    private int totalPrice;
    private int totalDiscount;
    private int totalPaymentPrice;

    @Builder
    public CartPriceInfoDto(int totalPrice, int totalDiscount, int totalPaymentPrice) {
        this.totalPrice = totalPrice;
        this.totalDiscount = totalDiscount;
        this.totalPaymentPrice = totalPaymentPrice;
    }
}
