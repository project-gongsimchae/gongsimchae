package techit.gongsimchae.domain.common.wishlist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.wishlist.dto.SubdivisionWishListRespDto;
import techit.gongsimchae.domain.common.wishlist.repository.WishListRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WishListService {

    private final WishListRepository wishListRepository;

    @Transactional(readOnly = true)
    public List<SubdivisionWishListRespDto> getSubdivisionWishLists(Long userId) {

        return wishListRepository.findWishListsByUserIdAndItemIsNull(userId)
                .stream().map(SubdivisionWishListRespDto::new).toList();
    }
}
