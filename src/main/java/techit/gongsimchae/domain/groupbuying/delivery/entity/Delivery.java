package techit.gongsimchae.domain.groupbuying.delivery.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;

@Entity
@Getter
@NoArgsConstructor
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @Builder
    public Delivery(DeliveryStatus deliveryStatus, OrderItem orderItem) {
        this.deliveryStatus = deliveryStatus;
        this.orderItem = orderItem;
    }

    public void updateDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
