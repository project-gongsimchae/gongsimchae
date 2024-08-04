package techit.gongsimchae.domain.portion.participants.dto;

import lombok.Getter;
import lombok.Setter;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.portion.participants.entity.Participants;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class ParticipantsDto {

    private Long id;
    private LocalDateTime createDate;
    private UserRespDtoWeb user;
    private SubdivisionDto subdivision;

    public ParticipantsDto(Participants participants) {
        this.id = participants.getId();
        this.createDate = participants.getCreateDate();
        this.user = new UserRespDtoWeb(participants.getUser());
        this.subdivision = new SubdivisionDto(participants.getSubdivision());
    }
}
