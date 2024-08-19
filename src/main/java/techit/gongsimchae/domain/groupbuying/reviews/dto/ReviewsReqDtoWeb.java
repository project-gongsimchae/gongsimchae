package techit.gongsimchae.domain.groupbuying.reviews.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class ReviewsReqDtoWeb {

    private String title;
    private int starPoint;
    private String content;
    private Boolean secretStatus = false;
    private String itemUID;
    private MultipartFile images;


}
