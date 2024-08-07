package techit.gongsimchae.domain.groupbuying.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import techit.gongsimchae.domain.groupbuying.coupon.entity.Coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponCustomRepository {

    Optional<Coupon> findByCouponCode(String couponCode);
}
