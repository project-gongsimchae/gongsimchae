package techit.gongsimchae.domain.groupbuying.payment.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.common.user.service.UserService;
import techit.gongsimchae.domain.groupbuying.cart.service.CartService;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.itemoption.service.ItemOptionService;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItemStatus;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderStatus;
import techit.gongsimchae.domain.groupbuying.orderitem.repository.OrderItemRepository;
import techit.gongsimchae.domain.groupbuying.orderitem.service.OrderItemService;
import techit.gongsimchae.domain.groupbuying.orders.dto.TempOrderItemDto;
import techit.gongsimchae.domain.groupbuying.orders.entity.Orders;
import techit.gongsimchae.domain.groupbuying.orders.repository.OrdersRepository;
import techit.gongsimchae.domain.groupbuying.payment.dto.PaymentDto;
import techit.gongsimchae.domain.portion.notifications.event.GroupBuyingNotiEvent;
import techit.gongsimchae.global.event.TargetCountAchievedEvent;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentsService {
    private final IamportClient iamportClient;
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;
    private final ItemOptionService itemOptionService;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final OrderItemService orderItemService;
    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    public List<PaymentDto> pgProvider(){
        return Arrays.asList(
                new PaymentDto("1", "카카오페이", "kakaopay.TC0ONETIME"),
                new PaymentDto("2", "토스페이", "tosspay.tosstest"),
                new PaymentDto("3","페이코","payco.PARTNERTEST")
        );
    }

    @Transactional
    public String createOrder(Long userId, List<TempOrderItemDto> tempOrderItems){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomWebException(ErrorMessage.USER_NOT_FOUND));


        int totalPrice = tempOrderItems.stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();



        Orders orders = Orders.builder()
                .merchantUid("order_no_"+ UUID.randomUUID().toString().substring(0,10))
                .orderStatus(OrderStatus.주문)
                .totalPrice(totalPrice)
                .user(user)
                .build();

        ordersRepository.save(orders);


        List<OrderItem> orderItems = tempOrderItems.stream()
                .map(tempItem -> OrderItem.builder()
                        .orders(orders)
                        .itemOption(itemOptionService.getItemOption(tempItem.getItemOptionId()))
                        .count(tempItem.getQuantity())
                        .price(tempItem.getPrice())
                        .build())
                .collect(Collectors.toList());


        orderItemRepository.saveAll(orderItems);
        return orders.getMerchantUid();
    }


    public IamportResponse<Payment> getPaymentInfo(String impUid) throws IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(impUid);
    }

    @Transactional
    public IamportResponse<Payment> verifyAndProcessPayment(String impUid, String merchantUid, int amount, List<TempOrderItemDto> tempOrderItems, Long userId) throws IamportResponseException, IOException {
        IamportResponse<Payment> paymentResponse = getPaymentInfo(impUid);
        Payment payment = paymentResponse.getResponse();

        Orders order = ordersRepository.findByMerchantUid(payment.getMerchantUid())
                .orElseThrow(() -> new IllegalStateException("주문 정보를 찾을 수 없습니다."));


        if (!validateAmount(order, payment)) {
            cancelPayment(impUid,"결제 금액이 일치하지 않습니다.");
            throw new IllegalStateException("결제 금액이 일치하지 않습니다.");
        }

        if (!payment.getMerchantUid().equals(merchantUid)) {
            cancelPayment(impUid,"주문번호가 일치하지 않습니다");
            throw new IllegalStateException("주문번호가 일치하지 않습니다.");
        }

        completeOrderStatus(payment);
        cartService.removeMultipleItems(userId, tempOrderItems.stream()
                .map(item -> item.getItemOptionId()).collect(Collectors.toList()));

        changeOrderItemStatus(order);
        checkTheTargetAmountAchieved(order);

        return paymentResponse;
    }


    private boolean validateAmount(Orders orders, Payment payment) {
        return orders.getTotalPrice() == payment.getAmount().intValue();
    }

    private void completeOrderStatus(Payment payment) {
        Orders order = ordersRepository.findByMerchantUid(payment.getMerchantUid())
                .orElseThrow(() -> new IllegalStateException("주문 정보를 찾을 수 없습니다."));

        order.updateStatus(OrderStatus.완료);
        order.updateImpUid(payment.getImpUid());
        ordersRepository.save(order);
    }


    public void cancelOrder(String impUid, String merchantUid) throws IamportResponseException, IOException {
        Orders orders = ordersRepository.findByMerchantUid(merchantUid)
                .orElseThrow(()-> new CustomWebException(ErrorMessage.USER_NOT_FOUND));

        orders.updateImpUid(impUid);
        orders.updateStatus(OrderStatus.취소);
        ordersRepository.save(orders);
    }

    private void cancelPayment(String impUid, String reason){
        try {
            CancelData cancelData = new CancelData(impUid, true, null);
            cancelData.setReason(reason);
            iamportClient.cancelPaymentByImpUid(cancelData);
        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * OrderItem의 상태를 결제완료로 바꾸는 메서드입니다.
     *
     * @param orders
     */
    private void changeOrderItemStatus(Orders orders) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrders(orders);

        for (OrderItem orderItem : orderItems) {
            orderItem.updateOrderItemStatus(OrderItemStatus.결제완료);
        }
    }

    /**
     * 각 아이템에 대한 주문량이 목표량에 도달했는지 체크하는 메서드입니다.
     *
     * @param orders
     */
    private void checkTheTargetAmountAchieved(Orders orders) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrders(orders);

        for (OrderItem orderItem : orderItems) {
            Item item = orderItem.getItemOption().getItem();

            if (item.getGroupBuyingQuantity() <= orderItemService.getCountCompletedOrderItemsWithItemId(item.getId()).getCompletionAmountCnt()) {
                groupBuyingSuccess(item);
            }
        }
    }

    /**
     * 주문량이 목표량에 도달했다면 공동구매 성공 이벤트 및 성공 알림 이벤트를 발행시킵니다.
     *
     * @param item
     */
    private void groupBuyingSuccess(Item item) {
        List<User> users = userService.findUsersByItemId(item.getId());
        log.info("usersize : {}", users.size());

        publisher.publishEvent(new TargetCountAchievedEvent(item.getId()));

        for (User user : users) {
            log.debug("group buying event");
            publisher.publishEvent(new GroupBuyingNotiEvent(user, item.getName() + "에 대한 공동구매가 성공했습니다! 배송 준비 중에 있으니 잠시만 기다려주세요."));
        }
    }
}