package techit.gongsimchae.domain.groupbuying.payment.service;

import com.siot.IamportRestClient.Iamport;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.groupbuying.cart.service.CartService;
import techit.gongsimchae.domain.groupbuying.itemoption.service.ItemOptionService;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderStatus;
import techit.gongsimchae.domain.groupbuying.orderitem.repository.OrderItemRepository;
import techit.gongsimchae.domain.groupbuying.orders.dto.TempOrderItemDto;
import techit.gongsimchae.domain.groupbuying.orders.entity.Orders;
import techit.gongsimchae.domain.groupbuying.orders.repository.OrdersRepository;
import techit.gongsimchae.domain.groupbuying.payment.dto.PaymentDto;
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
public class PaymentService {
    private final IamportClient iamportClient;
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;
    private final ItemOptionService itemOptionService;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
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
        return paymentResponse;
    }


    private boolean validateAmount(Orders orders, Payment payment) {
        return orders.getTotalPrice() == payment.getAmount().intValue();
    }

    private void completeOrderStatus(Payment payment) {
        Orders order = ordersRepository.findByMerchantUid(payment.getMerchantUid())
                .orElseThrow(() -> new IllegalStateException("주문 정보를 찾을 수 없습니다."));

        order.updateStatus(OrderStatus.완료);
        ordersRepository.save(order);
    }


    public void cancelOrder(String merchantUid) {
        Orders orders = ordersRepository.findByMerchantUid(merchantUid)
                .orElseThrow(()-> new CustomWebException(ErrorMessage.USER_NOT_FOUND));

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
}