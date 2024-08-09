package techit.gongsimchae.domain.groupbuying.coupon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.groupbuying.coupon.dto.CouponCreateReqDtoWeb;

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
    private String eventName;
    private String couponCode; // 쿠폰등록 번호

    /**
     * 생성자
     */
    public Coupon(CouponCreateReqDtoWeb dto) {
        this.discount = dto.getDiscount();
        this.maxDiscount = dto.getDiscount();
        this.expirationDate = dto.getExpirationDate();
        this.eventName = dto.getEventName();
        this.couponCode = dto.getCouponCode();
    }
}
