package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techit.gongsimchae.domain.common.user.service.UserService;
import techit.gongsimchae.domain.common.wishlist.service.WishListService;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionRespDto;
import techit.gongsimchae.domain.portion.subdivision.service.SubdivisionService;
import techit.gongsimchae.global.dto.PrincipalDetails;


@Controller
@RequiredArgsConstructor
public class SubdivisionController {

    private final SubdivisionService subdivisionService;
    private final WishListService wishListService;
    private final UserService userService;

    @GetMapping("/portioning/write")
    public String subdivisionRegisterForm() {

        return "portion/subdivisionRegister";
    }

    @PostMapping("/portioning/write")
    public String subdivisionRegister() {
        return "redirect:/portioning";
    }

    @GetMapping("/portioning/{UID}")
    public String subdivisionDetail(@PathVariable("UID") String UID,
                                     @AuthenticationPrincipal PrincipalDetails userDetails,
                                     Model model) {

        SubdivisionRespDto subdivisionRespDto = subdivisionService.findSubdivisionByUID(UID);
        model.addAttribute("subdivisionRespDto", subdivisionRespDto);

        boolean isLoggedIn = userDetails != null;

        if(isLoggedIn) {
            Long userId = userDetails.getAccountDto().getId();
            boolean isOwner = wishListService.isOwner(userId, subdivisionRespDto.getUID());
            model.addAttribute("isOwner", isOwner);

            boolean isWishListed = wishListService.isWishListed(userId, subdivisionRespDto.getUID());
            model.addAttribute("isWishListed", isWishListed);
        } else {
            model.addAttribute("isWishListed", false);
            model.addAttribute("isOwner", false);
        }

        return "portion/subdivisionDetail";
    }


    @GetMapping("/portioning/{UID}/join")
    public String subdivisionJoinChatRoom(@PathVariable("UID") String UID) {
        return "portion/chattingRoom";
    }


}
