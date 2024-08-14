package techit.gongsimchae.domain.groupbuying.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponCreateReqDtoWeb {
    private Integer discount;
    private Integer maxDiscount;
    private LocalDateTime expirationDate;
    private String couponName;
    private String couponCode; // 쿠폰등록 번호
    private MultipartFile eventBannerImage; // 배너 이미지
}
