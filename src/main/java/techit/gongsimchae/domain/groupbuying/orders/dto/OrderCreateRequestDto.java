package techit.gongsimchae.domain.groupbuying.orders.dto;

import lombok.Data;

@Data
public class OrderCreateRequestDto {
    private Long couponId;
    private Integer finalAmount;
}
