package techit.gongsimchae.domain.groupbuying.couponuser.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.groupbuying.coupon.entity.Coupon;
import techit.gongsimchae.domain.groupbuying.couponuser.entity.CouponUser;

public interface CouponUserRepository extends JpaRepository<CouponUser,Long> {
    List<CouponUser> findAllByCouponIn(List<Coupon> coupons);

    Optional<CouponUser> findByCouponAndUser(Coupon coupon, User user);

    Optional<CouponUser> findByUserIdAndCouponIdAndCouponStatus(Long userId, Long couponId, int couponStatus);
}
