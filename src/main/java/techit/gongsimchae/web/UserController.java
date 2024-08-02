package techit.gongsimchae.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techit.gongsimchae.domain.common.user.dto.UserInfoConfirmReqDtoWeb;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.common.user.dto.UserUpdateReqDtoWeb;
import techit.gongsimchae.domain.common.user.service.UserService;
import techit.gongsimchae.global.dto.PrincipalDetails;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Slf4j
public class UserController {
    private final UserService userService;

    /**
     * 유저가 자기자신 정보를 수정하는 페이지
     */
    @GetMapping("/info")
    public String InfoConfirmForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        UserRespDtoWeb user = userService.getUser(principalDetails.getUsername());
        model.addAttribute("user", user);

        return "user/info";
    }

    @PostMapping("/info")
    public String InfoConfirm(@Valid @ModelAttribute("user") UserInfoConfirmReqDtoWeb infoDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            log.debug("user info confirm {}", bindingResult.getAllErrors());
            return "user/info";
        }
        if (!userService.checkPassword(infoDto.getLoginId(), infoDto.getPassword())) {
            log.debug("user info confirm {}", bindingResult.getAllErrors());
            bindingResult.rejectValue("password","password.invalid","Invalid loginId or password");
            return "user/info";
        }

        return "redirect:/mypage/info/update";
    }

    @GetMapping("/info/update")
    public String userUpdateForm(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        UserRespDtoWeb user = userService.getUser(principalDetails.getUsername());
        model.addAttribute("user", user);
        return "user/update";
    }

    @PostMapping("/info/update")
    public String userUpdate(@Valid @ModelAttribute("user") UserUpdateReqDtoWeb updateDto, BindingResult bindingResult,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {

        if(bindingResult.hasErrors()) {
            log.info("user update form validation error {} ", bindingResult.getAllErrors());
            return "user/update";
        }

        if (!updateDto.getPasswordChange().equals(updateDto.getPasswordChangeConfirm())) {
            bindingResult.rejectValue("passwordConfirm","passwordConfirm.invalid","비밀번호가 일치하지 않습니다.");
            return "user/update";
        }

        if (!userService.checkPassword(principalDetails.getAccountDto().getUID(), updateDto.getPassword())) {
            bindingResult.rejectValue("password","password.invalid","비밀번호가 일치하지 않습니다.");
            return "user/update";
        }

        userService.updateInfo(updateDto, principalDetails);
        return "redirect:/mypage/info";
    }

    @PostMapping("/delete")
    public String userDelete(@AuthenticationPrincipal PrincipalDetails principalDetails, HttpServletRequest request, HttpServletResponse response) {
        userService.deleteUser(principalDetails);
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        return "redirect:/main";
    }

    /**
     * 1:1 문의
     */
    @GetMapping("/inquiry/list")
    public String inquiryList(Model model) {

        return "user/inquiryList";
    }

    @GetMapping("/inquiry/form")
    public String inquiryForm() {

        return "user/inquiryForm";
    }

    @PostMapping("/inquiry/form")
    public String inquires(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        return "redirect:/inquiry/list";
    }

    /**
     * 상품 후기
     */
    @GetMapping("/reviews")
    public String reviews(Model model) {
        return "user/reviews";
    }

    @GetMapping("/reviews/write")
    public String reviewForm() {
        return "user/reviewForm";
    }

    @PostMapping("/reviews/write")
    public String viewsWrite(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return "redirect:/mypage/reviews";
    }

    /**
     * 찜
     */

    @GetMapping("/pick/list")
    public String PickList() {
        return "user/pickList";
    }
}
