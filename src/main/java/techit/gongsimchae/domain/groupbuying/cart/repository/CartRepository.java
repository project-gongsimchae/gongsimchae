package techit.gongsimchae.domain.groupbuying.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.groupbuying.cart.entity.Cart;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUserIdAndItemId(Long userId,Long itemId);
    void deleteCartByUserIdAndItemId(Long userId, Long itemId);
    void deleteAllByUserIdAndItemIdIn(Long userId, List<Long> itemId);
    List<Cart> findByUserId(Long id);
}
