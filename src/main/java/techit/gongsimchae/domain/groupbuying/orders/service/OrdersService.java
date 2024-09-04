package techit.gongsimchae.domain.groupbuying.orders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.common.address.entity.Address;
import techit.gongsimchae.domain.common.address.repository.AddressRepository;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.groupbuying.cart.service.CartService;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;
import techit.gongsimchae.domain.groupbuying.orderitem.repository.OrderItemRepository;
import techit.gongsimchae.domain.groupbuying.orders.dto.OrdersPaymentDto;
import techit.gongsimchae.domain.groupbuying.orders.dto.TempOrderItemDto;
import techit.gongsimchae.domain.groupbuying.orders.dto.TempUserDeliveryDto;
import techit.gongsimchae.domain.groupbuying.orders.entity.Orders;
import techit.gongsimchae.domain.groupbuying.orders.repository.OrdersRepository;
import techit.gongsimchae.domain.groupbuying.payment.entity.Payment;
import techit.gongsimchae.domain.groupbuying.payment.repository.PaymentRepository;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CartService cartService;
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
            int originalPrice = item.getItemOption().getItem().getOriginalPrice();
            int discountRate = item.getItemOption().getItem().getDiscountRate();
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

    public List<TempOrderItemDto> createTempOrder(Long userId, List<Long> selectedItemOptionId){
        List<TempOrderItemDto> tempOrderItemDto = cartService.getCartItemFromOrderItem(userId, selectedItemOptionId);

        return tempOrderItemDto;
    }

    public int amountPrice(List<TempOrderItemDto> tempOrderItem){
        return tempOrderItem.stream()
                .mapToInt(TempOrderItemDto::getTotalPrice)
                .sum();
    }

    public TempUserDeliveryDto createTempUserDeliveryInfo(Long userid){
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new CustomWebException(ErrorMessage.USER_NOT_FOUND));
        Address address = addressRepository.findDefaultAddressByUser(userid)
                .orElseThrow(() -> new CustomWebException(ErrorMessage.USER_NOT_FOUND));

        String addressDetail = address.getAddress() + " " + address.getDetailAddress();
        return TempUserDeliveryDto.builder()
                .userName(user.getName())
                .userPhoneNumber(user.getPhoneNumber())
                .userEmail(user.getEmail())
                .recipientAddress(addressDetail)
                .recipientName(address.getReceiver())
                .recipientPhoneNumber(address.getPhoneNumber())
                .zipcode(address.getZipcode()).build();
    }
}
