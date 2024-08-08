package techit.gongsimchae.domain.common.inquiry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.inquiry.dto.InquiryCreateDtoWeb;
import techit.gongsimchae.domain.common.inquiry.dto.InquiryRespDtoWeb;
import techit.gongsimchae.domain.common.inquiry.dto.InquiryUpdateReqDtoWeb;
import techit.gongsimchae.domain.common.inquiry.entity.Inquiry;
import techit.gongsimchae.domain.common.inquiry.repository.InquiryRepository;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createInquiry(InquiryCreateDtoWeb dtoWeb, PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException("not found user"));
        Inquiry inquiry = new Inquiry(dtoWeb, user);
        inquiryRepository.save(inquiry);
    }

    public List<InquiryRespDtoWeb> getInquiry(PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException("not found user"));
        return inquiryRepository.findByUserId(user.getId()).stream().map(InquiryRespDtoWeb::new).collect(Collectors.toList());
    }

    public InquiryRespDtoWeb getInquiry(String UID, PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException("not found user"));
        InquiryRespDtoWeb inquiryRespDtoWeb = inquiryRepository.findByUID(UID)
                .map(InquiryRespDtoWeb::new)
                .orElseThrow(() -> new CustomWebException("not found inquiry"));
        inquiryRespDtoWeb.setEmail(user.getEmail());
        return inquiryRespDtoWeb;
    }

    @Transactional
    public void updateInquiry(String id, InquiryUpdateReqDtoWeb inquiryUpdateReqDtoWeb) {
        Inquiry inquiry = inquiryRepository.findByUID(id).orElseThrow(() -> new CustomWebException("not found inquiry"));
        inquiry.changeInfo(inquiryUpdateReqDtoWeb);
    }
    @Transactional
    public void deleteInquiry(String id) {
        inquiryRepository.deleteByUID(id);
    }
}
