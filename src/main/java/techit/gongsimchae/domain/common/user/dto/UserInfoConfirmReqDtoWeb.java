package techit.gongsimchae.domain.common.user.dto;

import lombok.Data;

@Data
public class UserInfoConfirmReqDtoWeb {
    private String loginId;
    private String password;
}
