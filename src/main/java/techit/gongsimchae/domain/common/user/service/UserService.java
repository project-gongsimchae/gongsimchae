package techit.gongsimchae.domain.common.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.user.dto.UserJoinReqDto;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 하는 로직
     */
    public void signup(UserJoinReqDto userJoinReqDto) {
        userJoinReqDto.setPassword(passwordEncoder.encode(userJoinReqDto.getPassword()));
        User user = new User(userJoinReqDto);
        userRepository.save(user);
    }

}
