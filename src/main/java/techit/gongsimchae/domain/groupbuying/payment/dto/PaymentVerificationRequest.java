package techit.gongsimchae.domain.groupbuying.payment.dto;

import lombok.Data;

@Data
public class PaymentVerificationRequest {
    private String merchantUid;
    private int amount;
}
