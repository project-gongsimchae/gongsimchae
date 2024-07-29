package techit.gongsimchae.domain.common.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.Address;
import techit.gongsimchae.domain.common.user.dto.UserJoinReqDtoWeb;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.common.user.dto.UserUpdateReqDtoWeb;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 하는 메서드
     */
    @Transactional
    public void signup(UserJoinReqDtoWeb joinDto) {
        joinDto.setPassword(passwordEncoder.encode(joinDto.getPassword()));
        Address address = new Address(joinDto);
        User user = new User(joinDto, address);
        userRepository.save(user);
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
        userRepository.deleteByUID(principalDetails.getAccountDto().getUID());

    }

    public boolean checkPassword(String uid, String password) {
        User user = userRepository.findByUID(uid).orElseThrow(() -> new CustomWebException("not found user"));
        return passwordEncoder.matches(password, user.getPassword());
    }
}
