package techit.gongsimchae.domain.portion.subdivision.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
public class SubdivisionUpdateReqDto {
    private Long id;
    private String title;
    private String content;
    private String address;
    private Double latitude;
    private Double longitude;
    private String sigungu;
    private Integer numOfParticipants;
    private Integer price;
    private List<MultipartFile> images;
    private List<Long> deleteImages;
    private String dong;
}
