package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import techit.gongsimchae.domain.common.user.dto.UserJoinReqDto;
import techit.gongsimchae.domain.common.user.service.UserService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute("user") UserJoinReqDto user) {
        return "login/signup";
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("user") UserJoinReqDto userJoinReqDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            log.info("sign up error {}", bindingResult.getAllErrors());
            return "login/signup";
        }

        if (!userJoinReqDto.getPassword().equals(userJoinReqDto.getPasswordConfirm())) {
            bindingResult.reject("password_not_confirm","비밀번호가 일치하지 않습니다.");
            return "login/singup";
        }
        userService.signup(userJoinReqDto);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "exception", required = false) String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "login/login";
    }

    @GetMapping("/denied")
    public String denied(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "exception", required = false) String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "login/denied";
    }


}
