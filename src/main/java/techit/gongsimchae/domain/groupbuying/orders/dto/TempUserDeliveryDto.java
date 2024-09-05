package techit.gongsimchae.domain.groupbuying.orders.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TempUserDeliveryDto {
    private String userName;
    private String userPhoneNumber;
    private String userEmail;
    private String recipientAddress;
    private String recipientName;
    private String recipientPhoneNumber;
    private String zipcode;
}
