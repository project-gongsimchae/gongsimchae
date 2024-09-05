package techit.gongsimchae.domain.groupbuying.coupon.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.groupbuying.coupon.entity.Coupon;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponRespDtoWeb {

    private Long id;
    private Integer discountRate;
    private Integer maxDiscount;
    private LocalDateTime expirationDate;
    private String couponName;
    private String couponCode; // 쿠폰등록 번호
    private List<String> applicableCategories = new ArrayList<>();
    private Integer couponStatus;
    public CouponRespDtoWeb(Coupon coupon) {
        this.id = coupon.getId();
        this.discountRate = coupon.getDiscountRate();
        this.maxDiscount = coupon.getMaxDiscount();
        this.expirationDate = coupon.getExpirationDate();
        this.couponName = coupon.getCouponName();
        this.couponCode = coupon.getCouponCode();
    }

    public void addCategories(String categoryName){
        applicableCategories.add(categoryName);
    }

}
