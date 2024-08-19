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
import org.springframework.web.bind.annotation.*;
import techit.gongsimchae.domain.common.address.dto.AddressCreateReqDtoWeb;
import techit.gongsimchae.domain.common.address.dto.AddressRespDtoWeb;
import techit.gongsimchae.domain.common.address.dto.AddressUpdateReqDtoWeb;
import techit.gongsimchae.domain.common.address.service.AddressService;
import techit.gongsimchae.domain.common.user.dto.UserInfoConfirmReqDtoWeb;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.common.user.dto.UserUpdateReqDtoWeb;
import techit.gongsimchae.domain.common.user.service.UserService;
import techit.gongsimchae.global.dto.PrincipalDetails;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Slf4j
public class UserController {
    private final UserService userService;
    private final AddressService addressService;

    /**
     * 유저 아이디, 비밀번호 확인
     */
    @GetMapping("/info")
    public String InfoConfirmForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        UserRespDtoWeb user = userService.getUser(principalDetails.getUsername());
        model.addAttribute("user", user);

        return "user/info";
    }

    @PostMapping("/info")
    public String InfoConfirm(@Valid @ModelAttribute("user") UserInfoConfirmReqDtoWeb infoDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.debug("user info confirm {}", bindingResult.getAllErrors());
            return "user/info";
        }
        if (!userService.checkPassword(infoDto.getLoginId(), infoDto.getPassword())) {
            log.debug("user info confirm {}", bindingResult.getAllErrors());
            bindingResult.rejectValue("password", "password.invalid", "Invalid loginId or password");
            return "user/info";
        }

        return "redirect:/mypage/info/update";
    }

    /**
     * 개인정보 수정
     */

    @GetMapping("/info/update")
    public String userUpdateForm(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        UserRespDtoWeb user = userService.getUser(principalDetails.getUsername());
        model.addAttribute("user", user);
        return "user/update";
    }

    @PostMapping("/info/update")
    public String userUpdate(@Valid @ModelAttribute("user") UserUpdateReqDtoWeb updateDto, BindingResult bindingResult,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {

        if (bindingResult.hasErrors()) {
            log.info("user update form validation error {} ", bindingResult.getAllErrors());
            return "user/update";
        }

        if (!updateDto.getPasswordChange().equals(updateDto.getPasswordChangeConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordConfirm.invalid", "비밀번호가 일치하지 않습니다.");
            return "user/update";
        }

        if (!userService.checkPassword(principalDetails.getUsername(), updateDto.getPassword())) {
            bindingResult.rejectValue("password", "password.invalid", "비밀번호가 일치하지 않습니다.");
            return "user/update";
        }

        userService.updateInfo(updateDto, principalDetails);
        return "redirect:/mypage/info";
    }

    /**
     * 회원탈퇴
     */

    @PostMapping("/delete")
    public String userDelete(@AuthenticationPrincipal PrincipalDetails principalDetails, HttpServletRequest request, HttpServletResponse response) {
        userService.deleteUser(principalDetails);
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/main";
    }

    /**
     * 배송지 관리
     */

    @GetMapping("/address")
    public String address(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        List<AddressRespDtoWeb> address = addressService.getAddresses(principalDetails);
        model.addAttribute("addresses", address);
        model.addAttribute("shippingAddress", new AddressCreateReqDtoWeb());
        return "user/address";
    }

    @PostMapping("/address")
    public String addAddress(@ModelAttribute("address") AddressCreateReqDtoWeb addressCreateReqDtoWeb, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.debug("address {}", addressCreateReqDtoWeb);
        addressService.addAddress(addressCreateReqDtoWeb, principalDetails);
        return "redirect:/mypage/address";
    }

    @GetMapping("/address/update/{id}")
    public String addressUpdateForm(@PathVariable("id") String id, Model model) {
        AddressRespDtoWeb address = addressService.getAddress(id);
        model.addAttribute("address", address);
        return "user/updateAddress";
    }

    @PostMapping("/address/update/{id}")
    public String addressUpdate(@PathVariable("id") String id, @Valid @ModelAttribute("address") AddressUpdateReqDtoWeb addressUpdateReqDtoWeb, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            log.debug("address update form validation error {} ", bindingResult.getAllErrors());
            return "user/updateAddress";
        }
        addressService.updateAddress(id, addressUpdateReqDtoWeb);
        return "redirect:/mypage/address";
    }

    @PostMapping("/address/delete/{id}")
    public String addressDelete(@PathVariable("id") String id) {
        addressService.deleteAddress(id);
        return "redirect:/mypage/address";
    }



}
