package techit.gongsimchae.domain.common.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import techit.gongsimchae.domain.common.wishlist.entity.WishList;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository <WishList, Long> {

    @Query("SELECT wl FROM WishList wl LEFT JOIN FETCH wl.subdivision s LEFT JOIN FETCH s.imageFileList WHERE wl.user.id = :userId AND wl.item IS NULL")
    List<WishList> findWishListsByUserIdAndItemIsNull(@Param("userId") Long userId);
}
