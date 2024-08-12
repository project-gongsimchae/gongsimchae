package techit.gongsimchae.domain.common.wishlist.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techit.gongsimchae.domain.common.wishlist.entity.WishList;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository <WishList, Long> {

    List<WishList> findWishListsByUserIdAndItemIsNull(Long userId);

    Optional<WishList> findByUserIdAndItemId(Long userId, Long itemId);

    @EntityGraph(attributePaths = {"user","item"})
    List<WishList> findWishListsItemByUserIdAndSubdivisionIsNull(Long userId);
}
