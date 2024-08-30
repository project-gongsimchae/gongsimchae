package techit.gongsimchae.domain.common.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.portion.notifications.dto.NotificationKeywordUserDto;

import java.util.List;

public interface UserCustomRepository {
    List<NotificationKeywordUserDto> findUsersByKeyword(String address, String keyword);
    Page<UserRespDtoWeb> findUsersWithReportCounts(Pageable pageable);

    List<User> findUsersByItemIdWithOrderItem(Long itemId);
}
