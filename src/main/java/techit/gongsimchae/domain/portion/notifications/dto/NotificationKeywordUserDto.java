package techit.gongsimchae.domain.portion.notifications.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.common.user.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationKeywordUserDto {
    private User user;
    private String keyword;

}
