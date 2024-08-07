package techit.gongsimchae.domain.common.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techit.gongsimchae.domain.common.wishlist.entity.WishList;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository <WishList, Long> {

    List<WishList> findWishListsByUserIdAndItemIsNull(Long userId);
}
