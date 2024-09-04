package techit.gongsimchae.domain.portion.subdivision.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    private String sigungu;
    @NotNull
    private Integer numOfParticipants;
    @NotNull
    private Integer price;
    private List<MultipartFile> images;
}
