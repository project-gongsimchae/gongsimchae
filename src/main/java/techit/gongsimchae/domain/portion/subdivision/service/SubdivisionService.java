package techit.gongsimchae.domain.portion.subdivision.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.portion.participants.service.ParticipantService;
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
    private final ParticipantService participantService;

    /**
     * URL의 Path 값으로 넘어온 UID로 DB에서 해당 소분 글을 찾아주는 메서드
     *
     */
    public SubdivisionRespDto findSubdivisionByUID(String UID) {

        Subdivision subdivision = subdivisionRepository.findByUID(UID).orElseThrow(() -> new CustomWebException("해당 소분 글을 찾을 수 없습니다."));

        return new SubdivisionRespDto(subdivision);

    }

    /**
     * UserId를 기반으로 자신이 작성한 소분 글 찾아주는 메서드
     *
     */
    public List<SubdivisionRespDto> findSubdivisionByUserId(Long userId) {

        return subdivisionRepository.findAllByUserId(userId).stream().map(SubdivisionRespDto::new).toList();
    }

    /**
     * UserId를 기반으로 해당 유저가 참여중인 소분글 가져오는 메서드
     *
     */
    public List<SubdivisionRespDto> findJoinSubdivisionByUserId(Long userId) {

        return participantService.findByUserId(userId).stream().map(participant -> participant.getSubdivision()).toList();
    }
}
