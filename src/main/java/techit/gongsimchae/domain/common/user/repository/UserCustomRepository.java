package techit.gongsimchae.domain.common.user.repository;

import techit.gongsimchae.domain.portion.notifications.dto.NotificationKeywordUserDto;

import java.util.List;

public interface UserCustomRepository {
    List<NotificationKeywordUserDto> findUsersByKeyword(String address, String keyword);
}
