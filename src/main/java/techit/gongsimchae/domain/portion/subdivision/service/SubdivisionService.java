package techit.gongsimchae.domain.portion.subdivision.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionDto;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;
import techit.gongsimchae.domain.portion.subdivision.repository.SubdivisionRepository;
import techit.gongsimchae.global.exception.CustomWebException;

@Service
@RequiredArgsConstructor
public class SubdivisionService {

    private final SubdivisionRepository subdivisionRepository;

    /**
     * URL의 Path 값으로 넘어온 UID로 DB에서 해당 소분 글을 찾아주는 메서드
     *
     */
    public SubdivisionDto findSubdivisionByUID(String UID) {

        Subdivision subdivision = subdivisionRepository.findByUID(UID).orElseThrow(() -> new CustomWebException("해당 소분 글을 찾을 수 없습니다."));

        return new SubdivisionDto(subdivision);

    }
}
