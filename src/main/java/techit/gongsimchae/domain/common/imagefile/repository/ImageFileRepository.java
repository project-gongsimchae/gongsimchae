package techit.gongsimchae.domain.common.imagefile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.groupbuying.coupon.entity.Coupon;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {

    ImageFile findByCoupon(Coupon coupon);
    ImageFile findByEvent(Event event);
}
