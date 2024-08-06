package techit.gongsimchae.domain.portion.participants.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.portion.participants.entity.Participant;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionRespDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ParticipantRespDto {

    private Long id;
    private LocalDateTime createDate;
    private UserRespDtoWeb user;
    private SubdivisionRespDto subdivision;

    public ParticipantRespDto(Participant participant) {
        this.id = participant.getId();
        this.createDate = participant.getCreateDate();
        this.user = new UserRespDtoWeb(participant.getUser());
        this.subdivision = new SubdivisionRespDto(participant.getSubdivision());
    }
}
