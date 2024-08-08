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
import techit.gongsimchae.global.exception.CustomWebException;

import java.util.List;

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
        Payment payment = paymentRepository.findByOrdersId(ordersId)
                .orElseThrow(() -> new CustomWebException("결제 정보를 찾지 못했습니다."));


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
        dto.setFinalPaymentAmount(payment.getAmount());
        dto.setPaymentType(payment.getPayType());

        return dto;
    }

}
