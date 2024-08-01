package techit.gongsimchae.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * 유저가 자기자신 정보를 보는 컨트롤러
     */
    @GetMapping("/info")
    public String account(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        String loginId = principalDetails.getUsername();
        UserRespDtoWeb user = userService.getUser(loginId);
        model.addAttribute("user", user);
        return "user/info";
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
    public String userDelete(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.deleteUser(principalDetails);
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
}
