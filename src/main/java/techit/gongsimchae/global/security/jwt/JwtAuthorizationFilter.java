package techit.gongsimchae.global.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import techit.gongsimchae.domain.common.refreshtoken.service.RefreshTokenService;
import techit.gongsimchae.global.dto.AccountDto;
import techit.gongsimchae.global.dto.PrincipalDetails;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProcess jwtProcess;
    private final RefreshTokenService refreshTokenService;

    public JwtAuthorizationFilter(JwtProcess jwtProcess, RefreshTokenService refreshTokenService) {
        this.jwtProcess = jwtProcess;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, String> cookieVerify = isCookieVerify(request);
        String accessToken = cookieVerify.get(JwtVO.ACCESS_CATEGORY);
        String refreshToken = cookieVerify.get(JwtVO.REFRESH_CATEGORY);
        if (accessToken != null || refreshToken != null) {
            try {
                if (!jwtProcess.isExpired(accessToken)) {
                    addSecurityContext(accessToken);
                }
            } catch (ExpiredJwtException e) {
                log.info("Expired JWT token {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                log.info("Invalid JWT token {}", e.getMessage());
            }

            try {
                if (!jwtProcess.isExpired(refreshToken)) {
                    if (refreshTokenService.isRefreshTokenExists(refreshToken)) {
                        addSecurityContext(refreshToken);
                    }
                }
            } catch (ExpiredJwtException e) {
                log.info("Expired JWT token {}", e.getMessage());
            }
        }
        filterChain.doFilter(request, response);

    }

    private void addSecurityContext(String token) {
        String loginId = jwtProcess.getLoginId(token);
        String role = jwtProcess.getRole(token);
        String uid = jwtProcess.getUID(token);
        PrincipalDetails principalDetails = new PrincipalDetails(new AccountDto(loginId, role, uid));
        Authentication authToken = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private Map<String, String> isCookieVerify(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Map<String, String> tokens = new HashMap<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(JwtVO.ACCESS_HEADER)) {
                    tokens.put(JwtVO.ACCESS_CATEGORY, cookie.getValue());
                }
                if (cookie.getName().equals(JwtVO.REFRESH_HEADER)) {
                    tokens.put(JwtVO.REFRESH_CATEGORY, cookie.getValue());
                }
            }
        }
        return tokens;
    }
}
