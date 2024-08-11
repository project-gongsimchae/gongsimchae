package techit.gongsimchae.domain.common.imagefile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.groupbuying.coupon.entity.Coupon;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.post.entity.Post;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ImageFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFilename;
    private String storeFilename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subdivision_id")
    private Subdivision subdivision;

    public ImageFile(String originalFilename, String storeFilename, User user) {
        this.originalFilename = originalFilename;
        this.storeFilename = storeFilename;
        this.user = user;
    }

    public ImageFile(String originalFilename, String storeFilename, Post post) {
        this.originalFilename = originalFilename;
        this.storeFilename = storeFilename;
        this.post = post;
    }

    public ImageFile(String originalFilename, String storeFilename, Item item) {
        this.originalFilename = originalFilename;
        this.storeFilename = storeFilename;
        this.item = item;
    }

    public ImageFile(String originalFilename, String storeFilename, Coupon coupon) {
        this.originalFilename = originalFilename;
        this.storeFilename = storeFilename;
        this.coupon = coupon;
    }

    public ImageFile(String originalFilename, String storeFilename, Event event) {
        this.originalFilename = originalFilename;
        this.storeFilename = storeFilename;
        this.event = event;
    }

    public ImageFile(String originalFilename, String storeFilename, Subdivision subdivision) {
        this.originalFilename = originalFilename;
        this.storeFilename = storeFilename;
        this.subdivision = subdivision;
    }
}
