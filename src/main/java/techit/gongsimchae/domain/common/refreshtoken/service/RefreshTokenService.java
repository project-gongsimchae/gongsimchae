package techit.gongsimchae.domain.common.refreshtoken.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import techit.gongsimchae.domain.common.refreshtoken.entity.RefreshTokenEntity;
import techit.gongsimchae.domain.common.refreshtoken.repository.RefreshTokenRepository;
import techit.gongsimchae.global.exception.CustomTokenException;


import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveRefreshToken(String loginId, String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(loginId, refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    public String getRefreshToken(String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new CustomTokenException("refresh token not found"));
        return refreshTokenEntity.getRefreshToken();
    }
    @Transactional
    public void deleteToken(String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new CustomTokenException("refresh token not found"));
        refreshTokenRepository.delete(refreshTokenEntity);
    }


    public boolean isRefreshTokenExists(String refreshToken) {
        Optional<RefreshTokenEntity> _refreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);
        return _refreshToken.isPresent();
    }
}
