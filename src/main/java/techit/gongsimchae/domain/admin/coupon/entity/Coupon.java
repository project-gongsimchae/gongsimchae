package techit.gongsimchae.domain.admin.coupon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter@Setter
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer discount;
    private Integer maxDiscount;
    private LocalDateTime expirationDate;
    private Integer couponStatus;
    private String eventName;
    private String eventImageUrl;


}
