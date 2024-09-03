package techit.gongsimchae.domain.portion.subdivision.dto;

import lombok.*;
import techit.gongsimchae.domain.common.es.entity.SubdivisionDocument;
import techit.gongsimchae.domain.common.imagefile.dto.ImageFileRespDto;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;
import techit.gongsimchae.domain.portion.subdivision.entity.SubdivisionType;

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
    private String sigungu;
    private Integer numOfParticipants;
    private Integer price;
    private Integer views;
    private String UID;
    private SubdivisionType subdivisionType;
    private UserRespDtoWeb user;
    private List<ImageFileRespDto> imageFileList;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public SubdivisionRespDto(Subdivision subdivision) {
        this.id = subdivision.getId();
        this.title = subdivision.getTitle();
        this.content = subdivision.getContent();
        this.address = subdivision.getAddress();
        this.latitude = subdivision.getLatitude();
        this.longitude = subdivision.getLongitude();
        this.numOfParticipants = subdivision.getNumOfParticipants();
        this.price = subdivision.getPrice();
        this.views = subdivision.getViews();
        this.UID = subdivision.getUID();
        this.subdivisionType = subdivision.getSubdivisionType();
        this.user = new UserRespDtoWeb(subdivision.getUser());
        this.imageFileList = subdivision.getImageFileList().stream().map(ImageFileRespDto::new).toList();
        this.createDate = subdivision.getCreateDate();
        this.updateDate = subdivision.getUpdateDate();
        this.sigungu = subdivision.getSigungu();
    }

    public SubdivisionRespDto(SubdivisionDocument subdivisionDocument) {
        this.id = subdivisionDocument.getId();
        this.title = subdivisionDocument.getTitle();
        this.content = subdivisionDocument.getContent();
        this.address = subdivisionDocument.getAddress();
        this.price = subdivisionDocument.getPrice();
        this.views = subdivisionDocument.getViews();
        this.UID = subdivisionDocument.getUid();
        this.subdivisionType = SubdivisionType.valueOf(subdivisionDocument.getSubdivisionType());
        if (subdivisionDocument.getUrl() != null) {
            this.imageFileList.add(ImageFileRespDto.builder().storeFilename(subdivisionDocument.getUrl()).build());
        }

    }
}
