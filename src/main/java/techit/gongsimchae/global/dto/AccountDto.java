package techit.gongsimchae.global.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.entity.UserRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String loginId;
    private String password;
    private UserRole role;
    private String phoneNumber;
    private String UID;

    public AccountDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.loginId = user.getLoginId();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.phoneNumber = user.getPhoneNumber();
        this.UID = user.getUID();
        this.nickname = user.getNickname();
    }

    public AccountDto(String loginId, String role, String UID) {
        this.loginId = loginId;
        this.role = UserRole.valueOf(role);
        this.UID = UID;
    }
}
