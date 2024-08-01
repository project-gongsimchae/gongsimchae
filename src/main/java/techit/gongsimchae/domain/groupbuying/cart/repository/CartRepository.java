package techit.gongsimchae.domain.groupbuying.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
