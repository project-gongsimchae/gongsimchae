package techit.gongsimchae.domain.portion.subdivision.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.user.entity.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class SubdivisionDto {

    private Long id;
    private String title;
    private String content;
    private String address;
    private Integer price;
    private Integer views;
    private String UID;
    private User user;
    private ImageFile imageFile;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
