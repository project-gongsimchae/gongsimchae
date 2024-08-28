package techit.gongsimchae.domain.groupbuying.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.groupbuying.delivery.entity.Delivery;
import techit.gongsimchae.domain.groupbuying.delivery.entity.DeliveryStatus;
import techit.gongsimchae.domain.groupbuying.delivery.repository.DeliveryRepository;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public Long registerDelivery(OrderItem orderItem) {
        Delivery delivery = Delivery.builder()
                .deliveryStatus(DeliveryStatus.배달준비중)
                .orderItem(orderItem)
                .build();

        Long id = deliveryRepository.save(delivery).getId();

        return id;
    }
}
