package techit.gongsimchae.domain.groupbuying.event.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EventCreateReqDtoWeb {
    @NotNull
    private String eventTypeName;
    @NotEmpty
    private List<String> categoryNames;
    @NotNull
    private String eventName;
    @NotNull
    private Integer discountRate;
    @NotNull
    private Integer maxDiscount;
    @NotNull
    private LocalDateTime expirationDate;
    private String couponName;
    private String couponCode;
    private MultipartFile eventBannerImage;
}
