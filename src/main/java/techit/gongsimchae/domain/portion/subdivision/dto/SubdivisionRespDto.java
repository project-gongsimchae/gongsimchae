package techit.gongsimchae.domain.portion.subdivision.dto;

import lombok.*;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SubdivisionRespDto {

    private Long id;
    private String title;
    private String content;
    private String address;
    private Double latitude;
    private Double longitude;
    private Integer price;
    private Integer views;
    private String UID;
    private UserRespDtoWeb user;
    private List<ImageFile> imageFileList;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public SubdivisionRespDto(Subdivision subdivision) {
        this.id = subdivision.getId();
        this.title = subdivision.getTitle();
        this.content = subdivision.getContent();
        this.address = subdivision.getAddress();
        this.latitude = subdivision.getLatitude();
        this.longitude = subdivision.getLongitude();
        this.price = subdivision.getPrice();
        this.views = subdivision.getViews();
        this.UID = subdivision.getUID();
        this.user = new UserRespDtoWeb(subdivision.getUser());
        this.imageFileList = subdivision.getImageFileList();
        this.createDate = subdivision.getCreateDate();
        this.updateDate = subdivision.getUpdateDate();
    }
}
