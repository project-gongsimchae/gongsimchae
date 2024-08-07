package techit.gongsimchae.domain.portion.participants.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.portion.participants.dto.ParticipantRespDto;
import techit.gongsimchae.domain.portion.participants.repository.ParticipantRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public List<ParticipantRespDto> findByUserId(Long userId) {

        return participantRepository.findAllByUserId(userId).stream().map(ParticipantRespDto::new).toList();
    }
}
