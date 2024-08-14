package techit.gongsimchae.domain.common.wishlist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;

import techit.gongsimchae.domain.common.wishlist.dto.SubdivisionWishListRespDto;
import techit.gongsimchae.domain.common.wishlist.entity.WishList;
import techit.gongsimchae.domain.common.wishlist.repository.WishListRepository;
import techit.gongsimchae.domain.groupbuying.item.dto.ItemRespDtoWeb;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.item.repository.ItemRepository;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;
import techit.gongsimchae.domain.portion.subdivision.repository.SubdivisionRepository;
import techit.gongsimchae.global.exception.CustomWebException;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishListService {

    private final WishListRepository wishListRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final SubdivisionRepository subdivisionRepository;


    @Transactional(readOnly = true)
    public List<SubdivisionWishListRespDto> getSubdivisionWishLists(Long userId) {

        return wishListRepository.findWishListsByUserIdAndItemIsNullAndSubdivisionDeleteStatusIsFalse(userId)
                .stream().map(SubdivisionWishListRespDto::new).toList();
    }


    /**
     * 아이템을 찜목록에 등록해놓는다
     */
    @Transactional
    public void pickItem(String itemId, PrincipalDetails principalDetails) {
        Item item = itemRepository.findByUID(itemId).orElseThrow(() -> new CustomWebException("not found item"));
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException("not found user"));
        Optional<WishList> _wishlist = wishListRepository.findByUserIdAndItemId(user.getId(), item.getId());
        if (_wishlist.isEmpty()) {
            WishList wishList = new WishList(item, user);
            wishListRepository.save(wishList);
        } else{
            wishListRepository.delete(_wishlist.get());
        }

    }

    /**
     * 찜목록에 넣은 아이템 가져오기
     */
    public List<ItemRespDtoWeb> getPickItems(PrincipalDetails principalDetails) {
        return wishListRepository.findWishListsItemByUserIdAndSubdivisionIsNull(principalDetails.getAccountDto().getId())
                .stream()
                .map(wishList -> new ItemRespDtoWeb(wishList.getItem()))
                .collect(Collectors.toList());

    }

    public boolean checkPickItem(String itemId, PrincipalDetails principalDetails) {
        Item item = itemRepository.findByUID(itemId).orElseThrow(() -> new CustomWebException("not found item"));
        Optional<WishList> _wishList = wishListRepository.findByUserIdAndItemId(principalDetails.getAccountDto().getId(), item.getId());
        if(_wishList.isEmpty()){
            return false;
        }
        return true;
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
