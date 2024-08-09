package techit.gongsimchae.domain.common.inquiry.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import techit.gongsimchae.domain.common.inquiry.dto.*;
import techit.gongsimchae.domain.common.inquiry.entity.Inquiry;
import techit.gongsimchae.domain.common.inquiry.entity.InquiryType;
import techit.gongsimchae.domain.common.inquiry.repository.InquiryRepository;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.mail.event.JoinMailEvent;
import techit.gongsimchae.domain.portion.notifications.event.InquiryNotiEvent;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void createInquiry(InquiryCreateDtoWeb dtoWeb, PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException("not found user"));
        if (dtoWeb.getInquiryType() == null) {
            dtoWeb.setInquiryType(InquiryType.ETC);
        }
        Inquiry inquiry = new Inquiry(dtoWeb, user);
        inquiryRepository.save(inquiry);
    }

    public List<InquiryRespDtoWeb> getInquiry(PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException("not found user"));
        return inquiryRepository.findByUserIdOrderByCreateDateDesc(user.getId()).stream().map(InquiryRespDtoWeb::new).collect(Collectors.toList());
    }

    public InquiryRespDtoWeb getInquiry(String UID) {
        return inquiryRepository.findInquiryByUID(UID);
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

    public Page<InquiryRespDtoWeb> getAllInquiries(Pageable pageable, String filter) {
        return inquiryRepository.findAllInquiries(pageable, filter);

    }
    @Transactional
    public void replyToInquiry(String inquiryId, InquiryAdminReplyReqDtoWeb dtoWeb) {
        Inquiry inquiry = inquiryRepository.findByUID(inquiryId).orElseThrow(() -> new CustomWebException("not found inquiry"));
        User user = userRepository.findById(inquiry.getUser().getId()).orElseThrow(() -> new CustomWebException("not found user"));
        log.debug("replyToInquiry dto {}", dtoWeb);
        inquiry.changeInfo(dtoWeb);
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        log.debug("inquiry event {} ", dtoWeb);
                        publisher.publishEvent(new InquiryNotiEvent(user, dtoWeb.getTitle()));
                    }
                }
        );

    }


}
