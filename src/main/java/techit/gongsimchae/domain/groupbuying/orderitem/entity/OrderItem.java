package techit.gongsimchae.domain.groupbuying.orderitem.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.groupbuying.itemoption.entity.ItemOption;
import techit.gongsimchae.domain.groupbuying.orders.entity.Orders;

@Entity
@Getter
@NoArgsConstructor
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer price;
    private Integer count;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemOption_id")
    private ItemOption itemOption;
    @Enumerated(EnumType.STRING)
    private OrderItemStatus orderItemStatus;

    @Builder
    public OrderItem(Integer price, Integer count, Orders orders, ItemOption itemOption, OrderItemStatus orderItemStatus) {
        this.price = price;
        this.count = count;
        this.orders = orders;
        this.itemOption = itemOption;
        this.orderItemStatus = orderItemStatus;
    }
}
