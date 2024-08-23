package techit.gongsimchae.global.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import techit.gongsimchae.domain.common.refreshtoken.service.RefreshTokenService;
import techit.gongsimchae.global.dto.AccountDto;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.security.jwt.JwtProcess;
import techit.gongsimchae.global.security.jwt.JwtVO;

import java.io.IOException;

@Component
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProcess jwtProcess;
    private final RefreshTokenService refreshTokenService;

    public OAuth2SuccessHandler(JwtProcess jwtProcess, RefreshTokenService refreshTokenService) {
        this.jwtProcess = jwtProcess;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        setDefaultTargetUrl("/");
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        AccountDto accountDto = principal.getAccountDto();
        log.info("account Dto {} ", accountDto);

        String accessToken = jwtProcess.createJwt(accountDto, JwtVO.ACCESS_CATEGORY);
        String refreshToken = jwtProcess.createJwt(accountDto, JwtVO.REFRESH_CATEGORY);

        response.addCookie(createCookie(JwtVO.ACCESS_HEADER, accessToken));
        response.addCookie(createCookie(JwtVO.REFRESH_HEADER, refreshToken));

        refreshTokenService.saveRefreshToken(accountDto.getLoginId(),refreshToken);
        getRedirectStrategy().sendRedirect(request, response, getDefaultTargetUrl());
    }


    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 24 * 7); // 60초 * 60 * 24 * 7 = 1주일
        cookie.setPath("/");
        cookie.setHttpOnly(true);


        return cookie;
    }
}
