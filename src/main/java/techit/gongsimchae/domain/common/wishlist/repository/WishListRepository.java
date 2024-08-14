package techit.gongsimchae.domain.common.wishlist.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import techit.gongsimchae.domain.common.wishlist.entity.WishList;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository <WishList, Long> {

    Optional<WishList> findByUserIdAndItemId(Long userId, Long itemId);

    @EntityGraph(attributePaths = {"user","item"})
    List<WishList> findWishListsItemByUserIdAndSubdivisionIsNull(Long userId);

    @Query("SELECT wl FROM WishList wl LEFT JOIN FETCH wl.subdivision s LEFT JOIN FETCH s.imageFileList WHERE wl.user.id = :userId AND wl.item IS NULL")
    List<WishList> findWishListsByUserIdAndItemIsNull(@Param("userId") Long userId);

    // User의 loginId와 Subdivision의 UID를 기준으로 찜 목록에 존재하는지 확인
    boolean existsByUserIdAndSubdivisionUID(Long userId, String subdivisionUID);

    void deleteByUserIdAndSubdivisionUID(Long userId, String subdivisionUID);
}
