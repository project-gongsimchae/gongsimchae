package techit.gongsimchae.domain.common.participate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.admin.item.entity.Item;
import techit.gongsimchae.domain.common.user.entity.User;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Participate extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer limitPerson;
    private LocalDateTime limitTime;
    private TransactionStatus transactionStatus;
    private PurchaseType purchaseType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
}
