package techit.gongsimchae.domain.portion.subdivision.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
public class SubdivisionReqDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @NotEmpty
    private String address;
    @NotEmpty
    private Double latitude;
    @NotEmpty
    private Double longitude;
    private String sigungu;
    @NotEmpty
    private Integer numOfParticipants;
    @NotEmpty
    private Integer price;
    private List<MultipartFile> images;
}
