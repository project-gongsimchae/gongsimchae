package techit.gongsimchae.domain.web.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import techit.gongsimchae.domain.common.user.dto.*;
import techit.gongsimchae.domain.common.user.service.UserService;
import techit.gongsimchae.global.util.CookieUtil;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute("user") UserJoinReqDtoWeb user) {
        return "login/signup";
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("user") UserJoinReqDtoWeb joinDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("sign up error {}", bindingResult.getAllErrors());
            return "login/signup";
        }
        log.info("sign up user {} " , joinDto);

        if (!joinDto.getPassword().equals(joinDto.getPasswordConfirm())) {
            bindingResult.reject("password_not_confirm", "비밀번호가 일치하지 않습니다.");
            return "login/signup";
        }
        if (!userService.verifiedCode(joinDto.getEmail(), joinDto.getAuthCode())) {
            bindingResult.reject("authCode_invalid", "인증번호가 일치하지 않습니다.");
            return "login/signup";
        }

        userService.signup(joinDto);

        return "redirect:/login";
    }

    /**
     * 아이디 찾기 페이지
     */
    @GetMapping("/find/id")
    public String findIdForm(@ModelAttribute("user") UserFindIdReqDtoWeb user) {
        return "login/findId";
    }

    @PostMapping("/find/id")
    public String findId(@Valid @ModelAttribute("user")UserFindIdReqDtoWeb findDto, BindingResult bindingResult,
                         HttpServletRequest request, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            log.debug("find id error {}", bindingResult.getAllErrors());
            return "login/findId";
        }
        if (!userService.checkUser(findDto)) {
            bindingResult.reject("not_found_user","일치하는 회원정보가 없습니다.");
            return "login/findId";
        }

        Map<String, String> idMap = new HashMap<>();
        idMap.put("email", findDto.getEmail());
        idMap.put("name", findDto.getName());

        CookieUtil.createCookie(idMap, FindIdVO.LOGINID_HEADER,response);

        return "redirect:/find/id/success";
    }


    /**
     * 아이디 알려주는 페이지
     * todo GET으로 받는건 위험하다 쿠키에서 데이터를 받는건 위험할 수도 있다. 다른 방법이 없나 생각해보자
     */
    @GetMapping("/find/id/success")
    public String notifyId(Model model, HttpServletRequest request, HttpServletResponse response ) {
        Map<String, String> map = CookieUtil.loadMapFromCookie(request, FindIdVO.LOGINID_HEADER);
        String email = map.get("email");
        String name = map.get("name");
        log.debug("success map {} ", map.toString());
        UserRespDtoWeb user = userService.getUser(name,email);

        String loginId = user.getLoginId();
        String notifyLoginId;
        if (loginId.length() % 2 == 0) {
            notifyLoginId= new StringBuilder(loginId).replace(loginId.length() / 2, loginId.length(), "*".repeat(loginId.length() / 2)).toString();
        } else notifyLoginId = new StringBuilder(loginId).replace(loginId.length() / 2, loginId.length(), "*".repeat(loginId.length() / 2+1)).toString();

        user.setLoginId(notifyLoginId);

        model.addAttribute("user", user);

        return "login/findIdSuccess";
    }

    /**
     * 전체 아이디를 이메일로 보내준다
     */
    @PostMapping("/find/id/send")
    public ResponseEntity<?> sendIdToEmail(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        log.debug("email {}", email);
        if (email != null) {
            userService.sendIdToEmail(email);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("이메일 파라미터가 누락되었습니다.");
        }
    }

    /**
     * 비밀번호 찾기
     */
    @GetMapping("/find/pw")
    public String findPwForm(@ModelAttribute("user") UserFindPwReqDtoWeb user){
        return "login/findPw";
    }

    /**
     * 고객정보가 일치하면 유저 email로 UUID Password를 보낸다
     */
    @PostMapping("/find/pw")
    public String findPw(@Valid @ModelAttribute("user") UserFindPwReqDtoWeb findDto, BindingResult bindingResult, HttpServletResponse response) {
        if(bindingResult.hasErrors()) {
            log.debug("find pw error {}", bindingResult.getAllErrors());
            return "login/findPw";
        }
        if (!userService.checkUser(findDto)) {
            bindingResult.reject("not_found_user","일치하는 회원정보가 없습니다.");
        }

        Map<String, String> idMap = new HashMap<>();
        idMap.put("email", findDto.getEmail());
        idMap.put("loginId", findDto.getLoginId());

        CookieUtil.createCookie(idMap, FindIdVO.PASSWORD_HEADER,response);
        userService.sendPasswordToEmail(findDto.getEmail());
        return "redirect:/find/pw/success";
    }

    @GetMapping("/find/pw/success")
    public String findPwSuccess(HttpServletRequest request, HttpServletResponse response,Model model) {
        String cookieEmail = CookieUtil.loadMapFromCookie(request, FindIdVO.PASSWORD_HEADER).get("email");
        log.debug("cookieEmail {} ", cookieEmail);
        UserRespDtoWeb user = userService.getUserByEmail(cookieEmail);
        model.addAttribute("email", user.getEmail());
        return "login/findPwSuccess";
    }


    /**
     * 이메일로 인증코드 보내는 로직
     */

    @PostMapping("/emails/verification-requests")
    public ResponseEntity<?> sendMessage(@RequestParam("email") @Valid String email) {
        log.debug("send email {}", email);
        userService.sendCodeToEmail(email);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 유저가 쓴 인증코드가 레디스에 저장된 코드와 맞는지 확인하는 로직
     */
    @GetMapping("/emails/verifications")
    public ResponseEntity<?> verificationEmail(@RequestParam("email") @Valid String email,
                                            @RequestParam("code") String authCode) {
        if(userService.verifiedCode(email, authCode))  return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


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
