package techit.gongsimchae.domain.groupbuying.event.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EventCreateReqDtoWeb {
    private String eventTypeName;
    private List<String> categoryNames;
    private String eventName;
    private Integer discountRate;
    private Integer maxDiscount;
    private LocalDateTime expirationDate;
    private String couponName;
    private String couponCode;
    private MultipartFile eventBannerImage;
}
