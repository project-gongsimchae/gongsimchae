package techit.gongsimchae.domain.common.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinReqDto {
    private String name;
    private String email;
    private String password;
    private String passwordConfirm;
    private String loginId;
    private String nickname;
    private String phoneNumber;

}
