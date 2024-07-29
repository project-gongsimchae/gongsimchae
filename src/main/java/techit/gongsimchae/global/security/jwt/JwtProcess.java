package techit.gongsimchae.global.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import techit.gongsimchae.domain.common.refreshtoken.repository.RefreshTokenRepository;
import techit.gongsimchae.global.dto.AccountDto;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProcess {

    private SecretKey secretKey;

    public JwtProcess(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String getLoginId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("loginId", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }
    public String getUID(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("uid", String.class);
    }
    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }


    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(AccountDto accountDto, String type) {
        String category;
        Long time;
        if (type.equals(JwtVO.ACCESS_CATEGORY)) {
            category = JwtVO.ACCESS_CATEGORY;
            time = JwtVO.ACCESS_TOKEN_EXPIRES_TIME;
        } else{
            category = JwtVO.REFRESH_CATEGORY;
            time = JwtVO.REFRESH_TOKEN_EXPIRES_TIME;
        }

        return Jwts.builder()
                .claim("category",category)
                .claim("loginId", accountDto.getLoginId())
                .claim("role", accountDto.getRole().name())
                .claim("uid",accountDto.getUID())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + time))
                .signWith(secretKey)
                .compact();
    }


}
