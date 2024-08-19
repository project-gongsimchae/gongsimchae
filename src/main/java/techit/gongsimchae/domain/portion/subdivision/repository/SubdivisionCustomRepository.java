package techit.gongsimchae.domain.portion.subdivision.repository;

import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionChatRoomRespDto;

import java.util.List;

public interface SubdivisionCustomRepository {
    List<SubdivisionChatRoomRespDto> findUserSubdivisions(Long userId);
}
