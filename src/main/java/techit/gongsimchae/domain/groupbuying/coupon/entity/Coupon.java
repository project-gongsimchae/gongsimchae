package techit.gongsimchae.domain.groupbuying.coupon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.groupbuying.coupon.dto.CouponCreateReqDtoWeb;
import techit.gongsimchae.domain.groupbuying.event.dto.EventCreateReqDtoWeb;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;

@Entity
@Getter
@NoArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;
    private Integer discountRate;
    private Integer maxDiscount;
    private LocalDateTime expirationDate;
    private String couponName;
    private String couponCode; // 쿠폰등록 번호

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;


    /**
     * 생성자
     */
    public Coupon(CouponCreateReqDtoWeb dto) {
        this.discountRate = dto.getDiscount();
        this.maxDiscount = dto.getDiscount();
        this.expirationDate = dto.getExpirationDate();
        this.couponName = dto.getCouponName();
        this.couponCode = dto.getCouponCode();
    }

    public Coupon(EventCreateReqDtoWeb eventDto){
        this.discountRate = eventDto.getDiscountRate();
        this.maxDiscount = eventDto.getMaxDiscount();
        this.expirationDate = eventDto.getExpirationDate();
        this.couponName = eventDto.getCouponName();
        this.couponCode = eventDto.getCouponCode();
    }

    public void connectEvent(Event event){
        this.event = event;
    }
}
