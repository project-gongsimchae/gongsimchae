package techit.gongsimchae.domain.common.inquiry.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import techit.gongsimchae.domain.common.inquiry.entity.InquiryType;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryCreateDtoWeb {

    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @NotNull
    private InquiryType inquiryType;
    private String UID;
    private String email;
    private List<MultipartFile> imageFiles = new ArrayList<>();
}
