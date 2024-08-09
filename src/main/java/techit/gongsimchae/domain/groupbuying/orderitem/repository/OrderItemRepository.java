package techit.gongsimchae.domain.groupbuying.orderitem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    List<OrderItem> findByOrdersId(Long id);
}
