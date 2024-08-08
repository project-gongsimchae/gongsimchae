package techit.gongsimchae.domain.groupbuying.orders.dto;

import lombok.Data;

@Data
public class OrdersPaymentDto {
    private Long orderId;
    private Integer totalPrice;
    private Integer discountAmount;
    private Integer couponDiscount;
    private Integer finalPaymentAmount;
    private String paymentType;
    
}
