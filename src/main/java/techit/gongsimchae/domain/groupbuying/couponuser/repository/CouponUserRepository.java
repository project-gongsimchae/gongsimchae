package techit.gongsimchae.domain.groupbuying.couponuser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.couponuser.entity.CouponUser;

public interface CouponUserRepository extends JpaRepository<CouponUser,Long> {
}
