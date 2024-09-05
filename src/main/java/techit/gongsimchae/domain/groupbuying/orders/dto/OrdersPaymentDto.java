package techit.gongsimchae.domain.groupbuying.orders.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrdersPaymentDto {
    private Long orderId;
    private Integer totalPrice;
    private Integer discountAmount;
    private Integer couponDiscount;
    private Integer finalPaymentAmount;
    private String paymentType;
    private String deliveryAddress;
    private String cancelReason;
}
