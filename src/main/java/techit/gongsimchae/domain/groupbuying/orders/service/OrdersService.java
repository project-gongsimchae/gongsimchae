package techit.gongsimchae.domain.groupbuying.orders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;
import techit.gongsimchae.domain.groupbuying.orderitem.repository.OrderItemRepository;
import techit.gongsimchae.domain.groupbuying.orders.dto.OrdersPaymentDto;
import techit.gongsimchae.domain.groupbuying.orders.entity.Orders;
import techit.gongsimchae.domain.groupbuying.orders.repository.OrdersRepository;
import techit.gongsimchae.domain.groupbuying.payment.entity.Payment;
import techit.gongsimchae.domain.groupbuying.payment.repository.PaymentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    public List<Orders> getUserOrders(Long userId){
        return ordersRepository.findByUserIdOrderByCreateDateDesc(userId);
    }

    public Orders getOrderDetail(Long ordersId, Long id) {
        return ordersRepository.findByIdAndUserId(ordersId,id).orElse(null);
    }

    public OrdersPaymentDto getOrdersPayment(Long ordersId){
        OrdersPaymentDto dto = new OrdersPaymentDto();
        List<OrderItem> orderItem = orderItemRepository.findByOrdersId(ordersId);
        Optional<Payment> payment = paymentRepository.findByOrdersId(ordersId);


        int totalPrice = 0;
        int totalDiscountPrice = 0;
        for (OrderItem item : orderItem){
            int originalPrice = item.getItem().getOriginalPrice();
            int discountRate = item.getItem().getDiscountRate();
            int quantity = item.getCount();

            totalPrice += originalPrice * quantity;
            totalDiscountPrice += (originalPrice * (100 - discountRate) / 100) * quantity;
        }

        dto.setOrderId(ordersId);
        dto.setTotalPrice(totalDiscountPrice);
        dto.setDiscountAmount(totalPrice - totalDiscountPrice);
        dto.setCouponDiscount(0);
        dto.setFinalPaymentAmount(payment.get().getAmount());
        dto.setPaymentType(payment.get().getPayType());

        return dto;
    }

}
