package techit.gongsimchae.domain.portion.subdivision.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SubdivisionRespDto {

    private Long id;
    private String title;
    private String content;
    private String address;
    private Integer price;
    private Integer views;
    private String UID;
    private UserRespDtoWeb user;
    private ImageFile imageFile;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public SubdivisionRespDto(Subdivision subdivision) {
        this.id = subdivision.getId();
        this.title = subdivision.getTitle();
        this.content = subdivision.getContent();
        this.address = subdivision.getAddress();
        this.price = subdivision.getPrice();
        this.views = subdivision.getViews();
        this.UID = subdivision.getUID();
        this.user = new UserRespDtoWeb(subdivision.getUser());
        this.imageFile = subdivision.getImageFile();
        this.createDate = subdivision.getCreateDate();
        this.updateDate = subdivision.getUpdateDate();
    }
}
