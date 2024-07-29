package techit.gongsimchae.domain.common.refreshtoken.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import techit.gongsimchae.domain.common.refreshtoken.entity.RefreshTokenEntity;
import techit.gongsimchae.domain.common.refreshtoken.repository.RefreshTokenRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RefreshTokenServiceTest {

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("Refresh Token Redis Test")
    void redis_token_test() throws Exception {
        // given
        String key = "token";
        refreshTokenService.saveRefreshToken(key, "테스트 토큰");

        // when
        Object refreshToken = refreshTokenService.getRefreshToken(key);
        System.out.println("refreshToken = " + refreshToken);

        // then
        Assertions.assertThat(refreshToken).isEqualTo("테스트 토큰");
    }

    @Test
    @DisplayName("RedisTemplate에 있는 Raw Data 꺼내기")
    void redis_raw_data_test() throws Exception {
        // given
        redisTemplate.opsForValue().set("key", "value");
        Object key = redisTemplate.opsForValue().get("key");
        System.out.println("key = " + key);

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity("hi", "123");

        refreshTokenService.saveRefreshToken("key1", refreshTokenEntity);
        Object refreshToken = refreshTokenService.getRefreshToken("key1");
        System.out.println("refreshToken = " + refreshToken);

        boolean result = refreshTokenService.existsByRefreshToken("123");
        System.out.println("result = " + result);

    }

    @Test
    @DisplayName("RedisRepository를 활용")
    void redis_repository_test() throws Exception {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity("hi", "123");
        refreshTokenRepository.save(refreshTokenEntity);
        Optional<RefreshTokenEntity> byRefreshToken = refreshTokenRepository.findByRefreshToken("123");
        RefreshTokenEntity refreshTokenEntity1 = byRefreshToken.get();
        System.out.println("refreshTokenEntity1 = " + refreshTokenEntity1)
        ;
    }
}