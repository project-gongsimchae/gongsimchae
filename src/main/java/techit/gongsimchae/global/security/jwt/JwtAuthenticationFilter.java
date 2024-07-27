package techit.gongsimchae.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import techit.gongsimchae.domain.common.refreshtoken.entity.RefreshTokenEntity;
import techit.gongsimchae.domain.common.refreshtoken.repository.RefreshTokenRepository;
import techit.gongsimchae.global.dto.AccountDto;
import techit.gongsimchae.global.dto.PrincipalDetails;

import java.io.IOException;
import java.util.Date;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private final JwtProcess jwtProcess;
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProcess jwtProcess, RefreshTokenRepository refreshTokenRepository) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtProcess = jwtProcess;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String loginId = request.getParameter("loginId");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password, null);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();
        AccountDto accountDto = principal.getAccountDto();
        log.info("jwt principal {} ", accountDto);
        String refreshToken = jwtProcess.createJwt(accountDto, JwtVO.REFRESH_CATEGORY);
        String accessToken = jwtProcess.createJwt(accountDto, JwtVO.ACCESS_CATEGORY);

        response.addCookie(createCookie(JwtVO.ACCESS_HEADER, accessToken));
        response.addCookie(createCookie(JwtVO.REFRESH_HEADER,refreshToken));
        saveRefreshToken(accountDto.getLoginId(), refreshToken);

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else{
            redirectStrategy.sendRedirect(request, response, "/");
        }

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

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
