package techit.gongsimchae.domain.groupbuying.payment.dto;

import lombok.Data;

@Data
public class PaymentDto {
    private String id;
    private String name;
    private String pgProvider;

    public PaymentDto(String id, String name, String pgProvider) {
        this.id = id;
        this.name = name;
        this.pgProvider = pgProvider;
    }
}


