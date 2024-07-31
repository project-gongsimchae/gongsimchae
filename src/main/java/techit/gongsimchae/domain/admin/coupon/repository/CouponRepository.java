package techit.gongsimchae.domain.admin.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.admin.coupon.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
