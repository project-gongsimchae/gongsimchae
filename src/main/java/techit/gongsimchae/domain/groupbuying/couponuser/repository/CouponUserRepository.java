package techit.gongsimchae.domain.groupbuying.couponuser.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.coupon.entity.Coupon;
import techit.gongsimchae.domain.groupbuying.couponuser.entity.CouponUser;

public interface CouponUserRepository extends JpaRepository<CouponUser,Long> {
    List<CouponUser> findAllByCouponIn(List<Coupon> coupons);
}
