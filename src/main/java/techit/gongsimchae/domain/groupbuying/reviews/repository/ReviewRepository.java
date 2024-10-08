package techit.gongsimchae.domain.groupbuying.reviews.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.reviews.entity.Review;
import techit.gongsimchae.domain.groupbuying.reviews.entity.SecretStatus;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByUserId(Long userId);
    Review findByItemUID(String uid);
    Review findByItemAndUser(Item item, User user);

    Review findByUserIdAndUID(Long userId, String uid);

    Review findByUserIdAndItemAndOrderItemId(Long userId, Item item, Long orderItemId);

    List<Review> findReviewsByItemAndSecretStatus(Item item, SecretStatus secretStatus);
}
