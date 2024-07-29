package techit.gongsimchae.domain.common.refreshtoken.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.refreshtoken.entity.RefreshTokenEntity;
import techit.gongsimchae.domain.common.refreshtoken.repository.RefreshTokenRepository;
import techit.gongsimchae.global.security.jwt.JwtVO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RefreshTokenService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public void saveRefreshToken(String loginId, Object refreshToken) {
        redisTemplate.opsForValue().set(loginId, refreshToken, JwtVO.REFRESH_TOKEN_EXPIRES_TIME, TimeUnit.MILLISECONDS);
    }

    public RefreshTokenEntity getRefreshToken(String loginId) {
        return (RefreshTokenEntity) redisTemplate.opsForValue().get(loginId);
    }

    public void deleteToken(String key) {
        redisTemplate.delete(key);
    }


    public boolean existsByRefreshToken(String loginId) {
        Long ttl = redisTemplate.getExpire(loginId, TimeUnit.MILLISECONDS);
        return ttl != null && ttl > 0;
    }
}
