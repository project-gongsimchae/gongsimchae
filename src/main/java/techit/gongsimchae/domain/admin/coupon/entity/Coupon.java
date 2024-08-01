package techit.gongsimchae.domain.admin.coupon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    private Integer discount;
    private Integer maxDiscount;
    private LocalDateTime expirationDate;
    private Integer couponStatus;
    private String eventName;
    private String eventImageUrl;


}
