package techit.gongsimchae.domain.common.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techit.gongsimchae.domain.common.wishlist.entity.WishList;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository <WishList, Long> {

    List<WishList> findWishListsByUserIdAndItemIsNull(Long userId);

    // User의 loginId와 Subdivision의 UID를 기준으로 찜 목록에 존재하는지 확인
    boolean existsByUserIdAndSubdivisionUID(Long userId, String subdivisionUID);

    void deleteByUserIdAndSubdivisionUID(Long userId, String subdivisionUID);
}
