package techit.gongsimchae.dummy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import techit.gongsimchae.domain.common.user.entity.JoinType;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.entity.UserRole;
import techit.gongsimchae.domain.common.user.entity.UserStatus;

import java.util.UUID;

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

    protected User adminUser(String loginId, String password){
        return User.builder()
                .loginId(loginId)
                .nickname("admin")
                .password("{noop}" + password)
                .email("admin@naver.com")
                .name("admin")
                .role(UserRole.ROLE_ADMIN)
                .UID(UUID.randomUUID().toString())
                .userStatus(UserStatus.NORMAL)
                .phoneNumber("010-1234-5678")
                .mannerPoint(0)
                .joinType(JoinType.NORMAL)
                .build();

    }

    protected User userUser(String loginId, String password) {


        return User.builder()
                .loginId(loginId)
                .nickname("user")
                .password("{noop}" + password)
                .email("user@naver.com")
                .name("user")
                .role(UserRole.ROLE_USER)
                .UID(UUID.randomUUID().toString())
                .userStatus(UserStatus.NORMAL)
                .phoneNumber("010-5678-1234")
                .mannerPoint(0)
                .joinType(JoinType.NORMAL)
                .build();
    }
}
