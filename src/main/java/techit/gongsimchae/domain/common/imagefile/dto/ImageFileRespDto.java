package techit.gongsimchae.domain.common.imagefile.dto;

import lombok.*;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ImageFileRespDto {
    private Long id;
    private String originalFilename;
    private String storeFilename;

    // 채팅방
    private String roomId;


    public ImageFileRespDto(ImageFile imageFile) {
        this.id = imageFile.getId();
        this.originalFilename = imageFile.getOriginalFilename();
        this.storeFilename = imageFile.getStoreFilename();
    }
    @Builder
    public ImageFileRespDto(Long id, String originalFilename, String storeFilename, String roomId) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.storeFilename = storeFilename;
        this.roomId = roomId;
    }
}
