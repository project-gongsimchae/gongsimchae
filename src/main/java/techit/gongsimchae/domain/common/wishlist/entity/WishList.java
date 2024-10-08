package techit.gongsimchae.domain.common.wishlist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class WishList extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subdivision_id")
    private Subdivision subdivision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public WishList(User user, Subdivision subdivision) {
        this.user = user;
        this.subdivision = subdivision;
    }

    public WishList(Item item, User user) {
        this.item = item;
        this.user = user;
    }
}
