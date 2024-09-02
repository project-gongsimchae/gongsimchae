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
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.imagefile.entity.S3VO;
import techit.gongsimchae.domain.common.imagefile.repository.ImageFileRepository;
import techit.gongsimchae.domain.common.imagefile.service.ImageS3Service;
import techit.gongsimchae.domain.common.inquiry.dto.InquiryAdminReplyReqDtoWeb;
import techit.gongsimchae.domain.common.inquiry.dto.InquiryCreateDtoWeb;
import techit.gongsimchae.domain.common.inquiry.dto.InquiryRespDtoWeb;
import techit.gongsimchae.domain.common.inquiry.dto.InquiryUpdateReqDtoWeb;
import techit.gongsimchae.domain.common.inquiry.entity.Inquiry;
import techit.gongsimchae.domain.common.inquiry.entity.InquiryType;
import techit.gongsimchae.domain.common.inquiry.repository.InquiryRepository;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.portion.notifications.event.InquiryNotiEvent;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;
    private final ImageS3Service imageS3Service;
    private final ImageFileRepository imageFileRepository;

    @Transactional
    public void createInquiry(InquiryCreateDtoWeb dtoWeb, PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException(ErrorMessage.USER_NOT_FOUND));
        if (dtoWeb.getInquiryType() == null) {
            dtoWeb.setInquiryType(InquiryType.ETC);
        }
        Inquiry savedInquiry = inquiryRepository.save(new Inquiry(dtoWeb, user));
        imageS3Service.storeFiles(dtoWeb.getImageFiles(), S3VO.INQUIRY_IMAGE_UPLOAD_DIRECTORY, savedInquiry);
    }

    public Page<InquiryRespDtoWeb> getInquiry(PrincipalDetails principalDetails, Pageable pageable) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException(ErrorMessage.USER_NOT_FOUND));
        return inquiryRepository.findByUserIdOrderByCreateDateDesc(user.getId(), pageable).map(InquiryRespDtoWeb::new);
    }

    public InquiryRespDtoWeb getInquiry(String UID) {
        return inquiryRepository.findInquiryByUID(UID);
    }

    @Transactional
    public void updateInquiry(String id, InquiryUpdateReqDtoWeb inquiryUpdateReqDtoWeb) {
        Inquiry inquiry = inquiryRepository.findByUID(id).orElseThrow(() -> new CustomWebException(ErrorMessage.INQUIRY_NOT_FOUND));
        inquiry.changeInfo(inquiryUpdateReqDtoWeb);
    }
    @Transactional
    public void deleteInquiry(String id) {
        Inquiry inquiry = inquiryRepository.findByUID(id).orElseThrow(() -> new CustomWebException(ErrorMessage.INQUIRY_NOT_FOUND));
        List<ImageFile> imageFiles = imageFileRepository.findByInquiry(inquiry);
        if (imageFiles != null) {
            imageFileRepository.deleteAll(imageFiles);
        }
        inquiryRepository.delete(inquiry);
    }

    public Page<InquiryRespDtoWeb> getAllInquiries(Pageable pageable, String filter) {
        return inquiryRepository.findAllInquiries(pageable, filter);

    }
    @Transactional
    public void replyToInquiry(String inquiryId, InquiryAdminReplyReqDtoWeb dtoWeb) {
        Inquiry inquiry = inquiryRepository.findByUID(inquiryId).orElseThrow(() -> new CustomWebException(ErrorMessage.INQUIRY_NOT_FOUND));
        User user = userRepository.findById(inquiry.getUser().getId()).orElseThrow(() -> new CustomWebException(ErrorMessage.USER_NOT_FOUND));
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

    public String isOwner(String inquiryId) {
        Inquiry inquiry = inquiryRepository.findByUID(inquiryId).orElseThrow(() -> new CustomWebException(ErrorMessage.INQUIRY_NOT_FOUND));
        return inquiry.getUser().getLoginId();
    }


}
