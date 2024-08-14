package techit.gongsimchae.domain.groupbuying.couponuser.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.groupbuying.coupon.entity.Coupon;
import techit.gongsimchae.domain.groupbuying.coupon.repository.CouponRepository;
import techit.gongsimchae.domain.groupbuying.couponuser.entity.CouponUser;
import techit.gongsimchae.domain.groupbuying.couponuser.repository.CouponUserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponUserService {

    private final CouponRepository couponRepository;
    private final CouponUserRepository couponUserRepository;

    public void deleteCouponUser(Long eventId){
        List<Coupon> coupons = couponRepository.findAllByEventId(eventId);
        List<CouponUser> couponUsers = couponUserRepository.findAllByCouponIn(coupons);
        couponUsers.forEach(CouponUser::setStatusDeleted);
    }

}
