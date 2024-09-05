package techit.gongsimchae.domain.groupbuying.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.payment.entity.Payments;

import java.util.Optional;

public interface PaymentsRepository extends JpaRepository<Payments,Long> {
    Optional<Payments> findByOrdersId(Long ordersId);
}
