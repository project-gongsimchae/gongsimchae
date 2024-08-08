package techit.gongsimchae.domain.groupbuying.coupon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.groupbuying.coupon.dto.CouponCreateReqDtoWeb;

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
    private String eventName;
    private String couponCode; // 쿠폰등록 번호

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_file_id")
    private ImageFile imageFile;

    /**
     * 생성자
     */
    public Coupon(CouponCreateReqDtoWeb dto) {
        this.discount = dto.getDiscount();
        this.maxDiscount = dto.getDiscount();
        this.expirationDate = dto.getExpirationDate();
        this.eventName = dto.getEventName();
        this.couponCode = dto.getCouponCode();
        // todo 추가해야됨
        this.imageFile = null;
    }


}
