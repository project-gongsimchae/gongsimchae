package techit.gongsimchae.domain.portion.subdivision.repository;

import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionChatRoomRespDto;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionRespDto;

import java.util.List;

public interface SubdivisionCustomRepository {
    List<SubdivisionChatRoomRespDto> findUserSubdivisions(Long userId);
}
