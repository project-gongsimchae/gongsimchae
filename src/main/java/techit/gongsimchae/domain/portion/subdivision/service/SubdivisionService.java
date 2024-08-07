package techit.gongsimchae.domain.portion.subdivision.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionRespDto;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;
import techit.gongsimchae.domain.portion.subdivision.repository.SubdivisionRepository;
import techit.gongsimchae.global.exception.CustomWebException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubdivisionService {

    private final SubdivisionRepository subdivisionRepository;


    public List<SubdivisionRespDto> getAllSubdivisions(){
        List<Subdivision> subdivisions = subdivisionRepository.findByOrderByCreateDateDesc();
        return subdivisions.stream()
                .map(SubdivisionRespDto::new)
                .collect(Collectors.toList());
    }

    /**
     * URL의 Path 값으로 넘어온 UID로 DB에서 해당 소분 글을 찾아주는 메서드
     *
     */
    public SubdivisionRespDto findSubdivisionByUID(String UID) {

        Subdivision subdivision = subdivisionRepository.findByUID(UID).orElseThrow(() -> new CustomWebException("해당 소분 글을 찾을 수 없습니다."));

        return new SubdivisionRespDto(subdivision);

    }
}
