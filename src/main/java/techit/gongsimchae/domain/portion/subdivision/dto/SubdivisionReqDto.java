package techit.gongsimchae.domain.portion.subdivision.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
public class SubdivisionReqDto {
    private String title;
    private String content;
    private String address;
    private Double latitude;
    private Double longitude;
    private Integer price;
    private List<MultipartFile> images;
}
