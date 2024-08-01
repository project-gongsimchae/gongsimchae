package techit.gongsimchae.domain.groupbuying.cart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.itemoption.entity.ItemOption;

@Entity
@Getter
@NoArgsConstructor
public class Cart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;
    private Integer count;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    /**
     * 차후에 orderItem과의 관계를 의논후 정의 **/
}
