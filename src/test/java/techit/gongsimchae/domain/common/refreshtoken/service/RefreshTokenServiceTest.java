package techit.gongsimchae.domain.common.refreshtoken.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import techit.gongsimchae.domain.common.refreshtoken.entity.RefreshTokenEntity;
import techit.gongsimchae.global.exception.CustomWebException;

@SpringBootTest
class RefreshTokenServiceTest {

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Test
    @DisplayName("Refresh Token Redis Test")
    void redis_token_test() throws Exception {
        // given
        String key = "token";
        refreshTokenService.saveRefreshToken("아이디", "테스트 토큰");

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

        Object refreshToken = refreshTokenService.getRefreshToken("key1");
        System.out.println("refreshToken = " + refreshToken);

        boolean result = refreshTokenService.existsByRefreshToken("123");
        System.out.println("result = " + result);

    }

    @Test
    @DisplayName("redisRepository로 save")
    void redis_repo_test() throws Exception {
        //given
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity("loginId", "token");
        refreshTokenService.saveRefreshToken("loginId", "token");
        String refreshToken = refreshTokenService.getRefreshToken("token");

        // when
        System.out.println("refreshToken = " + refreshToken);
        boolean b = refreshTokenService.existsByRefreshToken(refreshToken);
        System.out.println("b = " + b);

        // then
    }

    @Test
    @DisplayName("redisRepository로 삭제")
    void redis_repo_delete_test() throws Exception {
        //given
        String loginId = "loginId2";
        String token = "token2";
        refreshTokenService.saveRefreshToken(loginId, token);
        String refreshToken = refreshTokenService.getRefreshToken(token);

        System.out.println("refreshToken = " + refreshToken);
        boolean b = refreshTokenService.existsByRefreshToken(refreshToken);
        System.out.println("b = " + b);
        // when
        refreshTokenService.deleteToken(token);
        boolean b1 = refreshTokenService.existsByRefreshToken(token);
        System.out.println("b1 = " + b1);

        // then
        org.junit.jupiter.api.Assertions.assertThrows(CustomWebException.class, () -> refreshTokenService.getRefreshToken("token"));

    }

    @Test
    @DisplayName("만료기한 확인")
    void expiration_test() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(CustomWebException.class, () -> refreshTokenService.getRefreshToken("token"));

    }
}