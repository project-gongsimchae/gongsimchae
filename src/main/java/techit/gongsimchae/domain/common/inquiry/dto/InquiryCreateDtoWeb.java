package techit.gongsimchae.domain.common.inquiry.dto;

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

    private String title;
    private String content;
    private InquiryType inquiryType;
    private String UID;
    private String email;
    private List<MultipartFile> imageFiles = new ArrayList<>();
}
