package techit.gongsimchae.global.security.jwt;

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
import techit.gongsimchae.domain.common.refreshtoken.repository.RefreshTokenRepository;
import techit.gongsimchae.global.dto.AccountDto;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.TokenException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProcess jwtProcess;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthorizationFilter(JwtProcess jwtProcess, RefreshTokenRepository refreshTokenRepository) {
        this.jwtProcess = jwtProcess;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, String> cookieVerify = isCookieVerify(request);
        String accessToken = cookieVerify.get(JwtVO.ACCESS_CATEGORY);
        String refreshToken = cookieVerify.get(JwtVO.REFRESH_CATEGORY);
        if(accessToken != null || refreshToken != null) {
            if (!jwtProcess.isExpired(accessToken)) {
                addSecurityContext(accessToken);

            } else if (!jwtProcess.isExpired(refreshToken)) {
                if (refreshTokenRepository.existsByRefreshToken(refreshToken)) {
                    addSecurityContext(refreshToken);
                }
            }
        }

        filterChain.doFilter(request, response);

    }

    private void addSecurityContext(String token){
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
