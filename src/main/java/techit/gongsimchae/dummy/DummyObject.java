package techit.gongsimchae.dummy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.entity.UserRole;

import java.time.LocalDateTime;
@Slf4j
public class DummyObject {
    protected User newMockUser(Long id, String loginId, String name) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");
        return User.builder()
                .id(id)
                .loginId(loginId)
                .password(encPassword)
                .email(name+"@nate.com")
                .name(name)
                .role(UserRole.ROLE_USER)
                .build();
    }

    protected User newUser(String loginId, String password){
        return User.builder()
                .loginId(loginId)
                .password(password)
                .email("test"+"@nate.com")
                .name("test")
                .role(UserRole.ROLE_USER)
                .build();

    }
}
