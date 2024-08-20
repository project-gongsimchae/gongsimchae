package techit.gongsimchae.domain.groupbuying.reviews.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.reviews.entity.Reviews;

public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
    List<Reviews> findAllByUserId(Long userId);
    Reviews findByItemUID(String uid);
    Reviews findByItemAndUser(Item item, User user);

    Reviews findByUserIdAndUID(Long userId, String uid);

    Reviews findByUserIdAndItem(Long userId, Item item);
}
