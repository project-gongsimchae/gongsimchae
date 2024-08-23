package techit.gongsimchae.domain.groupbuying.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.payment.entity.Payment;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByOrdersId(Long ordersId);
}
