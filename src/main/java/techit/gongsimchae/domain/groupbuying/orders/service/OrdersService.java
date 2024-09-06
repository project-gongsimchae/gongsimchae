package techit.gongsimchae.domain.groupbuying.orders.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.common.address.entity.Address;
import techit.gongsimchae.domain.common.address.repository.AddressRepository;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.groupbuying.cart.service.CartService;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;
import techit.gongsimchae.domain.groupbuying.orderitem.repository.OrderItemRepository;
import techit.gongsimchae.domain.groupbuying.orders.dto.OrdersListResponseDto;
import techit.gongsimchae.domain.groupbuying.orders.dto.OrdersPaymentDto;
import techit.gongsimchae.domain.groupbuying.orders.dto.TempOrderItemDto;
import techit.gongsimchae.domain.groupbuying.orders.dto.TempUserDeliveryDto;
import techit.gongsimchae.domain.groupbuying.orders.entity.Orders;
import techit.gongsimchae.domain.groupbuying.orders.repository.OrdersRepository;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CartService cartService;
    private final IamportClient iamportClient;

    /**
     * 유저 아이디로 유저의 주문을 조회하는 메서드
     */
    public List<Orders> getUserOrders(Long userId){
        return ordersRepository.findByUserIdOrderByCreateDateDesc(userId);
    }

    /**
     * 주문 상세
     */
    public Orders getOrderDetail(Long ordersId, Long id) {
        return ordersRepository.findByIdAndUserId(ordersId,id).orElse(null);
    }

    /**
     *
     */
    public OrdersPaymentDto getOrdersPayment(Long ordersId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrdersId(ordersId);

        int totalOriginalPrice = 0;
        int totalDiscountedPrice = 0;

        for (OrderItem item : orderItems) {
            int itemPrice = calculateOrderItemPrice(item);
            int discountedPrice = calculateOrderItemDiscountPrice(item);
            int quantity = item.getCount();

            totalOriginalPrice += itemPrice * quantity;
            totalDiscountedPrice += discountedPrice * quantity;
        }

        int totalDiscountAmount = totalOriginalPrice - totalDiscountedPrice;

        try {
            IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(orderItems.get(0).getOrders().getImpUid());
            Payment payment = paymentResponse.getResponse();


            return OrdersPaymentDto.builder()
                    .orderId(ordersId)
                    .finalPaymentAmount(payment.getAmount().intValue())
                    .totalPrice(totalOriginalPrice)
                    .discountAmount(totalDiscountAmount)
                    .couponDiscount(0)
                    .paymentType(payment.getPgProvider())
                    .buyerName(payment.getBuyerName())
                    .buyerNumber(payment.getBuyerTel())
                    .deliveryAddress(payment.getBuyerAddr())
                    .cancelReason(payment.getCancelReason())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("결제 정보 조회 중 오류 발생", e);
        }
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

    public int calculateOrderItemPrice(OrderItem orderItem){
        return orderItem.getItemOption().getPrice() + orderItem.getItemOption().getItem().getOriginalPrice();
    }

    public int calculateOrderItemDiscountPrice(OrderItem orderItem){
        int originalPrice = calculateOrderItemPrice(orderItem);
        double discountRate = orderItem.getItemOption().getItem().getDiscountRate() / 100.0;
        int discountAmount = (int) (originalPrice * discountRate);
        return originalPrice - discountAmount;
    }

    public Page<OrdersListResponseDto> getOrders(Pageable pageable) {
        Page<Orders> orders = ordersRepository.findAll(pageable);

        return orders.map(order ->
                new OrdersListResponseDto(
                        order.getId(),
                        order.getOrderStatus(),
                        order.getImpUid(),
                        order.getMerchantUid(),
                        order.getTotalPrice(),
                        order.getUser().getId(),
                        order.getUser().getName(), // User 객체에서 이름 추출
                        order.getCreateDate()
                )
        );
    }

    public Orders getOrder(Long ordersId) {
        return ordersRepository.findById(ordersId).orElseThrow(
                () -> new CustomWebException(ErrorMessage.ORDERS_NOT_FOUND)
        );
    }

    public Orders getOrderByMerchantUid(String merchantUid) {
        return ordersRepository.findByMerchantUid(merchantUid)
                .orElseThrow(() -> new CustomWebException(ErrorMessage.ORDER_NOT_FOUND));

    }
}
