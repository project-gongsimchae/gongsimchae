package techit.gongsimchae.domain.portion.subdivision.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionChatRoomRespDto;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionReportRespDto;

import java.util.List;

public interface SubdivisionCustomRepository {
    List<SubdivisionChatRoomRespDto> findUserSubdivisions(Long userId);

    Page<SubdivisionReportRespDto> findMostFrequentReports(Pageable pageable);
}
