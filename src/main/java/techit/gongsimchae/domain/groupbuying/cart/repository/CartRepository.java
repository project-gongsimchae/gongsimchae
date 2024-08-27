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
    Optional<Cart> findByUserIdAndItemOptionId(Long userId,Long itemOptionId);
    void deleteCartByUserIdAndItemOptionId(Long userId, Long itemOptionId);
    void deleteAllByUserIdAndItemOptionIdIn(Long userId, List<Long> itemOptionId);
    List<Cart> findByUserId(Long id);
    List<Cart> findByUserIdAndItemOptionIdIn(Long userId, List<Long> selectedItemOption);
}
