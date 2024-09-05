package techit.gongsimchae.domain.groupbuying.orderitem.repository;

import org.hibernate.query.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;

import java.util.List;
import java.util.Optional;

import techit.gongsimchae.domain.groupbuying.orders.entity.Orders;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long>, OrderItemCustomRepository {
    List<OrderItem> findByOrdersId(Long orderId);

    List<OrderItem> findAllByOrdersIn(List<Orders> orders);

    List<OrderItem> findByOrdersIdIn(List<Long> ordersId);

    List<OrderItem> findAllByOrders(Orders orders);

}
