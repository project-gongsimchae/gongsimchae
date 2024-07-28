package techit.gongsimchae.web;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import techit.gongsimchae.domain.common.refreshtoken.entity.RefreshTokenEntity;
import techit.gongsimchae.domain.common.refreshtoken.repository.RefreshTokenRepository;
import techit.gongsimchae.domain.common.user.dto.UserJoinReqDto;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.common.user.service.UserService;
import techit.gongsimchae.global.dto.AccountDto;
import techit.gongsimchae.global.exception.CustomTokenException;
import techit.gongsimchae.global.security.jwt.JwtProcess;
import techit.gongsimchae.global.security.jwt.JwtVO;

import java.util.Date;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProcess jwtProcess;

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute("user") UserJoinReqDto user) {
        return "login/signup";
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("user") UserJoinReqDto userJoinReqDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("sign up error {}", bindingResult.getAllErrors());
            return "login/signup";
        }
        log.info("sign up user {} " , userJoinReqDto);

        if (!userJoinReqDto.getPassword().equals(userJoinReqDto.getPasswordConfirm())) {
            bindingResult.reject("password_not_confirm", "비밀번호가 일치하지 않습니다.");
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
        if (refreshTokenRepository.existsByRefreshToken(refresh)) {
            throw new CustomTokenException("invalid refresh token ");
        }

        String loginId = jwtProcess.getLoginId(refresh);
        String role = jwtProcess.getRole(refresh);
        String uid = jwtProcess.getUID(refresh);

        AccountDto accountDto = new AccountDto(loginId, role, uid);

        //make new JWT
        String newAccess = jwtProcess.createJwt(accountDto,JwtVO.ACCESS_CATEGORY);
        String newRefresh = jwtProcess.createJwt(accountDto,JwtVO.REFRESH_CATEGORY);

        refreshTokenRepository.deleteByRefreshToken(refresh);
        saveRefreshToken(loginId,newRefresh);
        //response
        response.addCookie(createCookie(JwtVO.ACCESS_HEADER, newAccess));
        response.addCookie(createCookie(JwtVO.REFRESH_HEADER, newRefresh));

        return "redirect:/";
    }

    private void saveRefreshToken(String loginId, String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(loginId, refreshToken,
                new Date(System.currentTimeMillis() + JwtVO.REFRESH_TOKEN_EXPIRES_TIME).toString());
        refreshTokenRepository.save(refreshTokenEntity);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 24 * 7); // 60초 * 60 * 24 * 7 = 1주일
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }


}
