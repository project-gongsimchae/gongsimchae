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
import techit.gongsimchae.domain.common.user.dto.*;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.common.wishlist.repository.WishListRepository;
import techit.gongsimchae.domain.groupbuying.coupon.entity.Coupon;
import techit.gongsimchae.domain.groupbuying.coupon.repository.CouponRepository;
import techit.gongsimchae.domain.groupbuying.couponuser.entity.CouponUser;
import techit.gongsimchae.domain.groupbuying.couponuser.repository.CouponUserRepository;
import techit.gongsimchae.domain.groupbuying.item.repository.ItemRepository;
import techit.gongsimchae.domain.common.user.mail.event.AuthCodeEvent;
import techit.gongsimchae.domain.common.user.mail.event.JoinMailEvent;
import techit.gongsimchae.domain.common.user.mail.event.LoginIdEvent;
import techit.gongsimchae.domain.common.user.mail.event.PasswordEvent;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.util.AuthCode;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final CouponRepository couponRepository;
    private final CouponUserRepository couponUserRepository;
    private final WishListRepository wishListRepository;
    private final ItemRepository itemRepository;

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

    /**
     * UUID값을 비밀번호로 바꾸고 그 값을 email로 보내는 메서드
     */
    @Transactional
    public void sendPasswordToEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomWebException("not found user"));
        String newPassword = UUID.randomUUID().toString();
        user.changePassword(passwordEncoder.encode(newPassword));
        publisher.publishEvent(new PasswordEvent(user.getEmail(), newPassword));
    }



    /**
     * 회원가입 하는 메서드
     */
    @Transactional
    public void signup(UserJoinReqDtoWeb joinDto) {
        joinDto.setPassword(passwordEncoder.encode(joinDto.getPassword()));
        User user = new User(joinDto);
        userRepository.save(user);
        sendJoinMail(joinDto);
    }

    public UserRespDtoWeb getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomWebException("not found user"));
        return new UserRespDtoWeb(user);
    }

    /**
     * 로그인 아이디로 유저정보 반환하는 메서드
     */
    public UserRespDtoWeb getUser(String loginId) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomWebException("not found user"));
        return new UserRespDtoWeb(user);
    }

    /**
     * loginId를 통해 유저를 찾고 더티체킹으로
     * nickname, email, phoneNumber, password 변경하는 메서드
     */
    @Transactional
    public void updateInfo(UserUpdateReqDtoWeb userUpdateReqDtoWeb, PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException("not found user"));
        userUpdateReqDtoWeb.setPasswordChange(passwordEncoder.encode(userUpdateReqDtoWeb.getPasswordChange()));
        user.changeInfo(userUpdateReqDtoWeb);
    }

    /**
     * loginId를 통해 유저 삭제하는 메서드
     */
    @Transactional
    public void deleteUser(PrincipalDetails principalDetails) {
        userRepository.deleteByLoginId(principalDetails.getUsername());

    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
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

    /**
     * 유저가 쓴 인증코드와 레디스에 저장한 인증코드가 맞는지 확인하는 메서드
     */
    public boolean verifiedCode(String email, String authCode) {
        String redisAuthCode = (String) redisTemplate.opsForValue().get(AUTH_CODE_PREFIX + email);
        return authCode.equals(redisAuthCode);
    }

    /**
     * email을 통해 user를 반환하는 메서드
     */
    public UserRespDtoWeb getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomWebException("not found user"));
        return new UserRespDtoWeb(user);
    }

    /**
     * 이름과 이메일로 유저를 확인해서 반환하는 메서드
     */
    public UserRespDtoWeb getUser(String name, String email ) {
        User user = userRepository.findByNameAndEmail(name, email).orElseThrow(() -> new CustomWebException("not found user"));
        return new UserRespDtoWeb(user);
    }

    public boolean checkUser(UserFindPwReqDtoWeb user) {
        return userRepository.existsByLoginIdAndEmail(user.getLoginId(), user.getEmail());
    }

    public boolean checkUser(UserFindIdReqDtoWeb user) {
        return userRepository.existsByNameAndEmail(user.getName(), user.getEmail());
    }

    /**
     * 전체 아이디를 이메일로 보내주는 이벤트
     */
    public void sendIdToEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomWebException("not found user"));
        publisher.publishEvent(new LoginIdEvent(user.getEmail(), user.getLoginId()));
    }

    /**
     * DB에 있는 유저 전체정보를 반환하는 메서드
     */
    public List<UserRespDtoWeb> getUsers() {
        return userRepository.findAll().stream().map(u -> new UserRespDtoWeb(u)).collect(Collectors.toList());
    }

    /**
     * 관리자가 유저의 정보를 바꾸는 메서드
     */
    @Transactional
    public void updateByAdmin(UserAdminUpdateReqDtoWeb updateDto) {
        User findUser = userRepository.findById(updateDto.getId()).orElseThrow(() -> new CustomWebException("not found user"));
        findUser.changeInfoByAdmin(updateDto);
    }

    public void addCoupon(String couponCode, PrincipalDetails principalDetails) {
        Coupon coupon = couponRepository.findByCouponCode(couponCode).orElseThrow(() -> new CustomWebException("not found coupon"));
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException("not found user"));
        CouponUser couponUser = new CouponUser(coupon, user);
        couponUserRepository.save(couponUser);

    }
}
