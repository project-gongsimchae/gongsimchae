package techit.gongsimchae.domain.groupbuying.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderStatus;
import techit.gongsimchae.domain.groupbuying.orders.entity.Orders;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,Long> {
    List<Orders> findByUserIdOrderByCreateDateDesc(Long id);

    Optional<Orders> findByIdAndUserId(Long id, Long userId);

    List<Orders> findAllByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);
}
