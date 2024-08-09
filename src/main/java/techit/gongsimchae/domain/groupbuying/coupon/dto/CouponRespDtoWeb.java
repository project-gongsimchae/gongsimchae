package techit.gongsimchae.domain.groupbuying.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.groupbuying.coupon.entity.Coupon;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponRespDtoWeb {

    private Long id;
    private Integer discount;
    private Integer maxDiscount;
    private LocalDateTime expirationDate;
    private String eventName;
    private String couponCode; // 쿠폰등록 번호
    private String eventBannerImage;

    public CouponRespDtoWeb(Coupon coupon, ImageFile imageFile) {
        this.id = coupon.getId();
        this.discount = coupon.getDiscount();
        this.maxDiscount = coupon.getMaxDiscount();
        this.expirationDate = coupon.getExpirationDate();
        this.eventName = coupon.getEventName();
        this.couponCode = coupon.getCouponCode();
        this.eventBannerImage = imageFile.getStoreFilename();
    }
}
