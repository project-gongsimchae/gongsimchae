package techit.gongsimchae.domain.portion.subdivision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.portion.subdivision.entity.SubdivisionType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubdivisionChatRoomRespDto {

    private Long subdivisionId;
    private String subdivisionTitle;
    private String subdivisionUID;
    private List<ImageFile> subdivisionImages = new ArrayList<>();
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private SubdivisionType subdivisionType;
    private String address;
    private Integer price;
    private String roomId;
    private Integer maxUserCnt;
    private Long chatRoomUsers;
}
