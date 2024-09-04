package techit.gongsimchae.domain.groupbuying.orders.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderStatus;

@Entity
@Getter
@NoArgsConstructor
public class Orders extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


    private String merchantUid;

    private Integer totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Orders(OrderStatus orderStatus, String merchantUid, Integer totalPrice, User user) {
        this.orderStatus = orderStatus;
        this.merchantUid = merchantUid;
        this.totalPrice = totalPrice;
        this.user = user;
    }

    public void updateStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
