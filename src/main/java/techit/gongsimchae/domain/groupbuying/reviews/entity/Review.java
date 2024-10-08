package techit.gongsimchae.domain.groupbuying.reviews.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.groupbuying.orderitem.entity.OrderItem;
import techit.gongsimchae.domain.groupbuying.reviews.dto.ReviewsReqDtoWeb;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private Integer starPoint;
    @Enumerated(EnumType.STRING)
    private SecretStatus secretStatus;
    @Column(unique = true)
    private String UID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    public Review(ReviewsReqDtoWeb reviewsReqDtoWeb, User user, Item item, OrderItem orderItem){
        this.title = reviewsReqDtoWeb.getTitle();
        this.starPoint = reviewsReqDtoWeb.getStarPoint();
        this.content = reviewsReqDtoWeb.getContent();
        this.secretStatus = reviewsReqDtoWeb.getSecretStatus() ? SecretStatus.SECRET : SecretStatus.NORMAL;
        this.UID = UUID.randomUUID().toString().substring(0,8);
        this.user = user;
        this.item = item;
        this.orderItem = orderItem;
    }

    public void update(ReviewsReqDtoWeb reviewsReqDtoWeb) {
        this.title = reviewsReqDtoWeb.getTitle();
        this.starPoint = reviewsReqDtoWeb.getStarPoint();
        this.content = reviewsReqDtoWeb.getContent();
        this.secretStatus = reviewsReqDtoWeb.getSecretStatus() ? SecretStatus.SECRET : SecretStatus.NORMAL;
    }
}
