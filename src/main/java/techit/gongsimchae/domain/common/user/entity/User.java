package techit.gongsimchae.domain.common.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.BaseEntity;
import techit.gongsimchae.domain.common.user.dto.UserJoinReqDto;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "users")
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String name;
    private String password;
    private String loginId;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String email;
    private String nickname;
    private String phoneNumber;
    private Integer mannerPoint;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    private String UID;

    public User(UserJoinReqDto joinReqDto) {
        this.name = joinReqDto.getName();
        this.email = joinReqDto.getEmail();
        this.password = joinReqDto.getPassword();
        this.loginId = joinReqDto.getLoginId();
        this.role = UserRole.ROLE_USER;
        this.nickname = joinReqDto.getNickname();
        this.phoneNumber = joinReqDto.getPhoneNumber();
        this.userStatus = UserStatus.NORMAL;
        this.UID = UUID.randomUUID().toString();
    }
}
