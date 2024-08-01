package techit.gongsimchae.domain.groupbuying.reviews.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.common.user.entity.User;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reviews extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer startPoint;
    private String content;
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


}
