package techit.gongsimchae.domain.web.portion;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import techit.gongsimchae.domain.common.wishlist.service.WishListService;
import techit.gongsimchae.global.dto.PrincipalDetails;


@Controller
@RequiredArgsConstructor
public class WishListController {
    private final WishListService wishListService;


    @PostMapping("/portioning/{subdivisionUID}/add")
    public String addToWishList(@AuthenticationPrincipal PrincipalDetails userDetails,
                                              @PathVariable String subdivisionUID) {

        Long userId = userDetails.getAccountDto().getId();
        wishListService.addToWishList(userId, subdivisionUID);
        return "redirect:/portioning/{subdivisionUID}";
    }

    @PostMapping("/portioning/{subdivisionUID}/remove")
    public String removeFromWishList(@AuthenticationPrincipal PrincipalDetails userDetails,
                                     @PathVariable String subdivisionUID) {
        Long userId = userDetails.getAccountDto().getId();
        wishListService.removeWishList(userId, subdivisionUID);
        return "redirect:/portioning/{subdivisionUID}";
    }

}
