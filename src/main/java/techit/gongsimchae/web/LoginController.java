package techit.gongsimchae.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
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
import techit.gongsimchae.domain.common.refreshtoken.service.RefreshTokenService;
import techit.gongsimchae.domain.common.user.dto.*;
import techit.gongsimchae.domain.common.user.service.UserService;
import techit.gongsimchae.global.dto.AccountDto;
import techit.gongsimchae.global.exception.CustomTokenException;
import techit.gongsimchae.global.security.jwt.JwtProcess;
import techit.gongsimchae.global.security.jwt.JwtVO;
import techit.gongsimchae.global.util.CookieUtil;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtProcess jwtProcess;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
            return "login/singup";
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
     * todo GET으로 받는건 위험하다
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
    public String findPw(@Valid @ModelAttribute("user") UserFindPwReqDtoWeb findDto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
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



    /**
     * Refresh 토큰 발급해주는 엔드포인트
     * 지금 사용할 일은 없는데 나중에 프론트엔드와 연결했을 때 사용
     */
    @PostMapping("/reissue")
    public String reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(JwtVO.REFRESH_HEADER)) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            throw new CustomTokenException("refresh token null");
        }
        try{
            jwtProcess.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new CustomTokenException("refresh token expired : " +e.getMessage());
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtProcess.getCategory(refresh);

        if (!category.equals(JwtVO.REFRESH_CATEGORY)) {
            throw new CustomTokenException("invalid refresh token ");
        }

        // DB에 저장되어 있는지 확인
        if (refreshTokenService.isRefreshTokenExists(refresh)) {
            throw new CustomTokenException("invalid refresh token ");
        }

        String loginId = jwtProcess.getLoginId(refresh);
        String role = jwtProcess.getRole(refresh);
        String uid = jwtProcess.getUID(refresh);

        AccountDto accountDto = new AccountDto(loginId, role, uid);

        //make new JWT
        String newAccess = jwtProcess.createJwt(accountDto,JwtVO.ACCESS_CATEGORY);
        String newRefresh = jwtProcess.createJwt(accountDto,JwtVO.REFRESH_CATEGORY);

        refreshTokenService.deleteToken(refresh);
        refreshTokenService.saveRefreshToken(loginId, newRefresh);
        //response
        response.addCookie(createCookie(JwtVO.ACCESS_HEADER, newAccess));
        response.addCookie(createCookie(JwtVO.REFRESH_HEADER, newRefresh));

        return "redirect:/";
    }




    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 24 * 7); // 60초 * 60 * 24 * 7 = 1주일
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }


}
