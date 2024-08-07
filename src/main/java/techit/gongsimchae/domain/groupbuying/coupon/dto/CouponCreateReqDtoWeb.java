package techit.gongsimchae.domain.groupbuying.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponCreateReqDtoWeb {
    private Integer discount;
    private Integer maxDiscount;
    private LocalDateTime expirationDate;
    private String eventName;
    private String couponCode; // 쿠폰등록 번호
}