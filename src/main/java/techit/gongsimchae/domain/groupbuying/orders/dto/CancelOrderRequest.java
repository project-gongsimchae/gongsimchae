package techit.gongsimchae.domain.groupbuying.orders.dto;

import lombok.Data;

@Data
public class CancelOrderRequest {
    private String impUid;
    private String merchantUid;
}
