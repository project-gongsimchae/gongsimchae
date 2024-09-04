package techit.gongsimchae.domain.groupbuying.couponcategory.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.couponcategory.entity.CouponCategory;

public interface CouponCategoryRepository extends JpaRepository<CouponCategory, Long> {
    List<CouponCategory> findAllByCouponId(Long id);
}
