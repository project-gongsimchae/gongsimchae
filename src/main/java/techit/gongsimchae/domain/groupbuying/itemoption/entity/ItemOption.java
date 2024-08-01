package techit.gongsimchae.domain.groupbuying.itemoption.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;

@Entity
@Getter
@NoArgsConstructor
public class ItemOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String option;
    private String content;
    private Integer price;
    @OneToOne
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;


}
