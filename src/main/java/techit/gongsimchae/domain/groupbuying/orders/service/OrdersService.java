package techit.gongsimchae.domain.groupbuying.orders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.groupbuying.orders.entity.Orders;
import techit.gongsimchae.domain.groupbuying.orders.repository.OrdersRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;

    public List<Orders> getUserOrders(Long userId){
        return ordersRepository.findByUserIdOrderByCreateDateDesc(userId);
    }

    public Orders getOrderDetail(Long ordersId, Long id) {
        return ordersRepository.findByIdAndUserId(ordersId,id).orElse(null);
    }

}
