package techit.gongsimchae.domain.common.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserFindIdReqDtoWeb {
    @NotEmpty
    private String name;
    @NotEmpty
    private String email;
}
