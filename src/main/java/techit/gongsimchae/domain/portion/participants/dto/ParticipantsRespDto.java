package techit.gongsimchae.domain.portion.participants.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.portion.participants.entity.Participants;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionRespDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ParticipantsRespDto {

    private Long id;
    private LocalDateTime createDate;
    private UserRespDtoWeb user;
    private SubdivisionRespDto subdivision;

    public ParticipantsRespDto(Participants participants) {
        this.id = participants.getId();
        this.createDate = participants.getCreateDate();
        this.user = new UserRespDtoWeb(participants.getUser());
        this.subdivision = new SubdivisionRespDto(participants.getSubdivision());
    }
}
