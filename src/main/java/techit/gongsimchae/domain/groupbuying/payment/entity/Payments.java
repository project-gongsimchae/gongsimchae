package techit.gongsimchae.domain.groupbuying.payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.groupbuying.orders.entity.Orders;

@Entity
@Getter
@NoArgsConstructor
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String payType;
    private Integer amount;
    private String paymentApproveId;
    private String orderName;
    private String customerName;
    private String successUrl;
    private String failUrl;
    private LocalDateTime payDate;
    private Boolean payStatus;
    @OneToOne
    @JoinColumn(name = "orders_id")
    private Orders orders;
}
