package techit.gongsimchae.domain.groupbuying.coupon.repository;

import techit.gongsimchae.domain.groupbuying.coupon.dto.CouponRespDtoWeb;

import java.util.List;

public interface CouponCustomRepository {
    List<CouponRespDtoWeb> findCouponsByUserId(Long userId);
}
