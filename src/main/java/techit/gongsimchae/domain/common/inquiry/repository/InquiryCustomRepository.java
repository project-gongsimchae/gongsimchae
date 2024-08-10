package techit.gongsimchae.domain.common.inquiry.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import techit.gongsimchae.domain.common.inquiry.dto.InquiryRespDtoWeb;
import techit.gongsimchae.domain.common.inquiry.dto.InquirySearchForm;

import java.util.List;

public interface InquiryCustomRepository {
    Page<InquiryRespDtoWeb> findAllInquiries(Pageable pageable, String filter);

    InquiryRespDtoWeb findInquiryByUID(String id);
}
