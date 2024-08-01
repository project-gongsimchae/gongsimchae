package techit.gongsimchae.domain.groupbuying.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.coupon.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon,Long> {
}
