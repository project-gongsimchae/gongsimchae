package techit.gongsimchae.domain.common.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserInfoConfirmReqDtoWeb {
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
}
