package techit.gongsimchae.domain.common.inquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.common.inquiry.entity.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
