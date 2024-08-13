package techit.gongsimchae.domain.common.wishlist.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.common.wishlist.dto.SubdivisionWishListRespDto;
import techit.gongsimchae.domain.common.wishlist.entity.WishList;
import techit.gongsimchae.domain.common.wishlist.repository.WishListRepository;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;
import techit.gongsimchae.domain.portion.subdivision.repository.SubdivisionRepository;
import techit.gongsimchae.global.exception.CustomWebException;


import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final WishListRepository wishListRepository;
    private final SubdivisionRepository subdivisionRepository;
    private final UserRepository userRepository;

    public List<SubdivisionWishListRespDto> getSubdivisionWishLists(Long userId) {
        return wishListRepository.findWishListsByUserIdAndItemIsNull(userId)
                .stream().map(SubdivisionWishListRespDto::new).toList();
    }

    // 찜 여부 확인
    public boolean isWishListed(Long userId, String subdivisionUID) {
        return wishListRepository.existsByUserIdAndSubdivisionUID(userId, subdivisionUID);
    }


    @Transactional
    public void addToWishList(Long userId, String subdivisionUID) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomWebException("User not found"));

        Subdivision subdivision = subdivisionRepository.findByUID(subdivisionUID)
                .orElseThrow(() -> new CustomWebException("Subdivision not found"));

        WishList wishList = new WishList(user, subdivision);
        wishListRepository.save(wishList);
    }

    @Transactional
    public void removeWishList(Long userId, String subdivisionUID) {
        wishListRepository.deleteByUserIdAndSubdivisionUID(userId, subdivisionUID);
    }


    public boolean isOwner(Long userId, String subdivisionUID) {
        Subdivision subdivision = subdivisionRepository.findByUID(subdivisionUID)
                .orElseThrow(() -> new CustomWebException("Subdivision not found"));
        return subdivision.getUser().getId().equals(userId);
    }
}
