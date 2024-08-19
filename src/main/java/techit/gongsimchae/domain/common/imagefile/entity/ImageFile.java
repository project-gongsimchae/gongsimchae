package techit.gongsimchae.domain.common.imagefile.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.groupbuying.event.entity.Event;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.post.entity.Post;
import techit.gongsimchae.domain.groupbuying.reviews.entity.Reviews;
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
    private Integer imageFileStatus;

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
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subdivision_id")
    private Subdivision subdivision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviews_id")
    private Reviews reviews;

    public ImageFile(String originalFilename, String storeFilename, User user) {
        this.originalFilename = originalFilename;
        this.storeFilename = storeFilename;
        this.user = user;
        this.imageFileStatus = 0;
    }

    public ImageFile(String originalFilename, String storeFilename, Post post) {
        this.originalFilename = originalFilename;
        this.storeFilename = storeFilename;
        this.post = post;
        this.imageFileStatus = 0;
    }

    public ImageFile(String originalFilename, String storeFilename, Item item) {
        this.originalFilename = originalFilename;
        this.storeFilename = storeFilename;
        this.item = item;
        this.imageFileStatus = 0;
    }

    public ImageFile(String originalFilename, String storeFilename, Subdivision subdivision) {
        this.originalFilename = originalFilename;
        this.storeFilename = storeFilename;
        this.subdivision = subdivision;
        this.imageFileStatus = 0;
    }

    public ImageFile(String originalFilename, String storeFilename, Event event) {
        this.originalFilename = originalFilename;
        this.storeFilename = storeFilename;
        this.event = event;
        this.imageFileStatus = 0;
    }

    public ImageFile(String originalFilename, String storeFilename, Reviews reviews) {
        this.originalFilename = originalFilename;
        this.storeFilename = storeFilename;
        this.reviews = reviews;
        this.imageFileStatus = 0;
    }

    public void setStatusDeleted(){
        this.imageFileStatus = 1;
    }
}
