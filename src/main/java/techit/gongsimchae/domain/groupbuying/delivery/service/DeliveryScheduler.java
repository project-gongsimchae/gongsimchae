package techit.gongsimchae.domain.groupbuying.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.groupbuying.delivery.entity.Delivery;
import techit.gongsimchae.domain.groupbuying.delivery.entity.DeliveryStatus;
import techit.gongsimchae.domain.groupbuying.delivery.repository.DeliveryRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeliveryScheduler {

    private final DeliveryRepository deliveryRepository;

    @Scheduled(cron = "0 */10 * * * *", zone = "Asia/Seoul")
    @Transactional
    public void changeStatusPreparedDeliveryStatus() {
        List<Delivery> deliveryList = deliveryRepository.findAllByDeliveryStatus(DeliveryStatus.배달준비중);

        ZonedDateTime tenMinutesAgo = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).minusMinutes(10);

        for (Delivery delivery : deliveryList) {
            if (delivery.getCreateDate().isBefore(tenMinutesAgo.toLocalDateTime())) {
                delivery.updateDeliveryStatus(DeliveryStatus.배달중);
            }
        }
    }

    @Scheduled(cron = "0 */10 * * * *", zone = "Asia/Seoul")
    @Transactional
    public void changeStatusDeliveryStatus() {
        List<Delivery> deliveryList = deliveryRepository.findAllByDeliveryStatus(DeliveryStatus.배달중);

        ZonedDateTime twentyMinutesAgo = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).minusMinutes(20);

        for (Delivery delivery : deliveryList) {
            if (delivery.getCreateDate().isBefore(twentyMinutesAgo.toLocalDateTime())) {
                delivery.updateDeliveryStatus(DeliveryStatus.배달완료);
            }
        }
    }
}
