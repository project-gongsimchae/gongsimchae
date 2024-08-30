package techit.gongsimchae.domain.groupbuying.orderitem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;

import java.util.List;
import techit.gongsimchae.domain.groupbuying.orders.entity.Orders;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long>, OrderItemCustomRepository {
    List<OrderItem> findByOrdersId(Long id);

    List<OrderItem> findAllByOrdersIn(List<Orders> orders);
}
