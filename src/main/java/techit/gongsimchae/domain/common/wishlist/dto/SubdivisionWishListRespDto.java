package techit.gongsimchae.domain.common.wishlist.dto;

import lombok.*;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.common.wishlist.entity.WishList;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionRespDto;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SubdivisionWishListRespDto {

    private Long id;
    private SubdivisionRespDto subdivision;
    private UserRespDtoWeb user;

    public SubdivisionWishListRespDto(WishList wishList) {
        this.id = wishList.getId();
        this.subdivision = new SubdivisionRespDto(wishList.getSubdivision());
        this.user = new UserRespDtoWeb(wishList.getUser());
    }
}
