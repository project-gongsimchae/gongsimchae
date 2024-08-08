package techit.gongsimchae.domain.common.inquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.common.inquiry.entity.Inquiry;

import java.util.List;
import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByUserId(Long id);

    Optional<Inquiry> findByUID(String uid);

    void deleteByUID(String id);
}
