package techit.gongsimchae.domain.common.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import techit.gongsimchae.domain.Address;
import techit.gongsimchae.domain.common.user.dto.UserJoinReqDtoWeb;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.common.user.dto.UserUpdateReqDtoWeb;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.mail.event.AuthCodeEvent;
import techit.gongsimchae.domain.mail.event.JoinMailEvent;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.util.AuthCode;

import java.time.Duration;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private static final Long authExpiration = 1000* 60 * 5L; // 5분


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 6자리 인증코드를 만들고 redis에 저장
     */
    @Transactional
    public void sendCodeToEmail(String email) {
        String sixCode = AuthCode.createSixCode();
        log.debug("six code {} ", sixCode);
        redisTemplate.opsForValue().set(AUTH_CODE_PREFIX + email, sixCode, Duration.ofMillis(authExpiration));
        publisher.publishEvent(new AuthCodeEvent(email,sixCode));
    }

    public boolean verifiedCode(String email, String authCode) {
        String redisAuthCode = (String) redisTemplate.opsForValue().get(AUTH_CODE_PREFIX + email);
        return authCode.equals(redisAuthCode);
    }

    /**
     * 회원가입 하는 메서드
     */
    @Transactional
    public void signup(UserJoinReqDtoWeb joinDto) {
        joinDto.setPassword(passwordEncoder.encode(joinDto.getPassword()));
        Address address = new Address(joinDto);
        User user = new User(joinDto, address);
        userRepository.save(user);
        sendJoinMail(joinDto);
    }

    /**
     * 유저정보 반환하는 메서드
     */
    public UserRespDtoWeb getUser(String loginId) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomWebException("not found user"));
        return new UserRespDtoWeb(user);
    }

    /**
     * UID를 통해 유저를 찾고 더티체킹으로
     * nickname, email, phoneNumber, password 변경하는 메서드
     */
    @Transactional
    public void updateInfo(UserUpdateReqDtoWeb userUpdateReqDtoWeb, PrincipalDetails principalDetails) {
        User user = userRepository.findByUID(principalDetails.getAccountDto().getUID()).orElseThrow(() -> new CustomWebException("not found user"));
        userUpdateReqDtoWeb.setChangePassword(passwordEncoder.encode(userUpdateReqDtoWeb.getChangePassword()));
        user.changeInfo(userUpdateReqDtoWeb);
    }

    /**
     * UID를 통해 유저 삭제하는 메서드
     */
    @Transactional
    public void deleteUser(PrincipalDetails principalDetails) {
        userRepository.deleteByLoginId(principalDetails.getUsername());

    }

    /**
     * 개인정보 수정할 때 기존비밀번호와 입력한 비밀번확 맞는지 확인하는 메서드
     */

    public boolean checkPassword(String loginId, String password) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomWebException("not found user"));
        return passwordEncoder.matches(password, user.getPassword());
    }

    /**
     * 회원가입하면 축하합니다 메일이 가도록 이벤트 처리하는 메서드
     */
    private void sendJoinMail(UserJoinReqDtoWeb joinDto) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        log.debug("joinDto email {} ", joinDto.getEmail());
                        publisher.publishEvent(new JoinMailEvent(joinDto));
                    }
                }
        );
    }
}
