package techit.gongsimchae.domain.groupbuying.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.coupon.entity.Coupon;

import java.util.Optional;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponCustomRepository {

    Optional<Coupon> findByCouponCode(String couponCode);
    Optional<Coupon> findByEvent(Event event);
}
