package techit.gongsimchae.domain.groupbuying.delivery.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techit.gongsimchae.domain.groupbuying.delivery.entity.Delivery;
import techit.gongsimchae.domain.groupbuying.delivery.entity.DeliveryStatus;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Page<Delivery> findByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable);

    List<Delivery> findAllByDeliveryStatus(DeliveryStatus deliveryStatus);
}
