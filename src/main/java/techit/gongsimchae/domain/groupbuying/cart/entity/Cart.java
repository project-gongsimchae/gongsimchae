package techit.gongsimchae.domain.groupbuying.cart.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;

@Entity
@Table(name = "cart")
@Getter
@NoArgsConstructor
public class Cart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;
    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",unique = false)
    private User user;

    /**
     * 차후에 orderItem과의 관계를 의논후 정의 **/

    public Cart(Integer count, Item item, User user) {
        this.count = count;
        this.item = item;
        this.user = user;
    }

    public void addCount(int count) {
        this.count += count;
    }

    public void updateCount(int count) {
        this.count = count;
    }
}
