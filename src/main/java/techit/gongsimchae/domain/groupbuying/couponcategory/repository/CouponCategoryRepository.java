package techit.gongsimchae.domain.groupbuying.couponcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.couponcategory.entity.CouponCategory;

public interface CouponCategoryRepository extends JpaRepository<CouponCategory, Long> {
}
