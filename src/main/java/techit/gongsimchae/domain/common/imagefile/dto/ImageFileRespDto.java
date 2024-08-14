package techit.gongsimchae.domain.common.imagefile.dto;

import lombok.*;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ImageFileRespDto {
    private Long id;
    private String originalFilename;
    private String storeFilename;

    public ImageFileRespDto(ImageFile imageFile) {
        this.id = imageFile.getId();
        this.originalFilename = imageFile.getOriginalFilename();
        this.storeFilename = imageFile.getStoreFilename();
    }
}
