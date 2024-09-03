package techit.gongsimchae.domain.portion.subdivision.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import techit.gongsimchae.domain.common.es.repository.SubElasticRepository;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.imagefile.repository.ImageFileRepository;
import techit.gongsimchae.domain.common.imagefile.service.ImageS3Service;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.portion.chatroom.service.ChatRoomService;
import techit.gongsimchae.domain.portion.chatroomuser.event.RoomUserEndEvent;
import techit.gongsimchae.domain.portion.notifications.dto.NotificationKeywordUserDto;
import techit.gongsimchae.domain.portion.notifications.event.KeywordNotiEvent;
import techit.gongsimchae.domain.portion.subdivision.dto.*;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;
import techit.gongsimchae.domain.portion.subdivision.entity.SubdivisionType;
import techit.gongsimchae.domain.portion.subdivision.repository.SubdivisionRepository;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SubdivisionService {

    private final SubdivisionRepository subdivisionRepository;
    private final UserRepository userRepository;
    private final ImageS3Service imageS3Service;
    private final ImageFileRepository imageFileRepository;
    private final ChatRoomService chatRoomService;
    private final ApplicationEventPublisher publisher;
    private final SubElasticRepository subElasticRepository;

    /**
     * 소분글 메인페이지에 모든 소분글들을 보여주는 메서드
     */
    public Page<SubdivisionRespDto> getAllSubdivisions(SubSearchDto searchDto, Pageable pageable) {
        return subdivisionRepository.searchAndSortSubdivisions(searchDto, pageable);
    }

    /**
     * URL의 Path 값으로 넘어온 UID로 DB에서 해당 소분 글을 찾아주는 메서드
     */
    public SubdivisionRespDto findSubdivisionByUID(String UID) {

        Subdivision subdivision = subdivisionRepository.findByUID(UID).orElseThrow(() -> new CustomWebException("해당 소분 글을 찾을 수 없습니다."));

        return new SubdivisionRespDto(subdivision);

    }

    /**
     * UserId를 기반으로 자신이 작성한 소분 글 찾아주는 메서드
     */
    public List<SubdivisionRespDto> findSubdivisionByUserId(Long userId) {

        return subdivisionRepository.findAllByUserIdAndDeleteStatusIsFalse(userId).stream().map(SubdivisionRespDto::new).toList();
    }

    /**
     * 소분글을 저장하는 메서드
     */
    @Transactional
    public String saveSubdivision(SubdivisionReqDto subdivisionReqDto,
                                  Long userId) {


        User user = userRepository.findById(userId).orElseThrow(() -> new CustomWebException("not found user"));

        Subdivision subdivision = Subdivision.builder()
                .title(subdivisionReqDto.getTitle())
                .content(subdivisionReqDto.getContent())
                .address(subdivisionReqDto.getAddress())
                .latitude(subdivisionReqDto.getLatitude())
                .longitude(subdivisionReqDto.getLongitude())
                .numOfParticipants(subdivisionReqDto.getNumOfParticipants())
                .price(subdivisionReqDto.getPrice())
                .views(0)
                .UID(UUID.randomUUID().toString())
                .subdivisionType(SubdivisionType.RECRUITING)
                .sigungu(subdivisionReqDto.getSigungu())
                .user(user)
                .dong(getDong(subdivisionReqDto.getAddress()))
                .build();

        Subdivision savedSubdivision = subdivisionRepository.save(subdivision);

        notifyKeyword(savedSubdivision.getSigungu(), savedSubdivision.getTitle(), savedSubdivision.getUID());

        // chatroom 생성
        chatRoomService.create(savedSubdivision);
        List<ImageFile> imageFiles = imageS3Service.storeFiles(subdivisionReqDto.getImages(), "images", subdivision);
        saveSubDocument(savedSubdivision, imageFiles);

        return subdivision.getUID();
    }

    private void saveSubDocument(Subdivision subdivision, List<ImageFile> imageFiles) {
        String url = null;
        if (!imageFiles.isEmpty()) {
            url = imageFiles.get(0).getStoreFilename();
        }
        subElasticRepository.createSubDocument(subdivision, url);
    }

    private void updateSubDocument(Subdivision subdivision, List<ImageFile> imageFiles) {
        String url = null;
        if (!imageFiles.isEmpty()) {
            url = imageFiles.get(0).getStoreFilename();
        }
        subElasticRepository.updateSubdivision(subdivision, url);
    }

    /**
     * 소분글을 수정하는 메서드
     */
    @Transactional
    public String updateSubdivision(SubdivisionUpdateReqDto subdivisionUpdateReqDto) {

        log.debug("subdivisionUpdateReqDto {}", subdivisionUpdateReqDto);
        subdivisionUpdateReqDto.setDong(getDong(subdivisionUpdateReqDto.getAddress()));

        Subdivision subdivision = subdivisionRepository.findById(subdivisionUpdateReqDto.getId()).orElseThrow(() -> new CustomWebException("Subdivision not found"));
        subdivision.updateSubdivision(subdivisionUpdateReqDto);

        notifyKeyword(subdivision.getSigungu(), subdivision.getTitle(), subdivision.getUID());

        List<ImageFile> imageFiles = imageS3Service.storeFiles(subdivisionUpdateReqDto.getImages(), "images", subdivision);

        if (!Objects.isNull(subdivisionUpdateReqDto.getDeleteImages())) {
            for (Long deleteImageId : subdivisionUpdateReqDto.getDeleteImages()) {

                ImageFile imageFile = imageFileRepository.findById(deleteImageId).orElseThrow(() -> new CustomWebException("ImageFile not found"));

                imageS3Service.deleteFile(imageFile.getStoreFilename());
                imageS3Service.deleteFileFromDb(imageFile);
            }
        }
        updateSubDocument(subdivision, imageFiles);

        return subdivision.getUID();
    }

    /**
     * 소분글을 삭제처리하는 메서드
     * 실제로 삭제하는 건 아니고 상태를 변환하는 것이다
     */
    @Transactional
    public void deleteSubdivision(String UID) {

        Subdivision subdivision = subdivisionRepository.findByUID(UID).orElseThrow(() -> new CustomWebException("Subdivision not found"));

        subdivision.deleteSubdivision();

        updateSubDocument(subdivision, null);
    }

    /**
     * 마이페이지에서 참여중인 소분글 찾는 메서드
     */
    public List<SubdivisionChatRoomRespDto> getUserSubdivisions(PrincipalDetails principalDetails) {
        return subdivisionRepository.findUserSubdivisions(principalDetails.getAccountDto().getId());
    }

    /**
     * 소분글 상태를 바꾸는 메서드
     */
    @Transactional
    public void changeStatus(String uid, String status) {
        Subdivision subdivision = subdivisionRepository.findByUID(uid).orElseThrow(() -> new CustomWebException(ErrorMessage.SUBDIVISION_NOT_FOUND));
        subdivision.changeType(status);
        subElasticRepository.updateSubdivision(subdivision);
        if (SubdivisionType.valueOf(status).equals(SubdivisionType.END)) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publisher.publishEvent(new RoomUserEndEvent(subdivision.getId(), subdivision.getTitle()));
                }
            });


        }

    }

    /**
     * 신고를 많이받은 소분글 불러오는 메서드
     */
    public Page<SubdivisionReportRespDto> getMostReported(Pageable pageable) {
        return subdivisionRepository.findMostFrequentReports(pageable);
    }

    /**
     * 키워드에 등록된 글이 올라오면 알림이 가도록 설정
     */

    private void notifyKeyword(String sigungu, String title, String url) {
        List<NotificationKeywordUserDto> users = userRepository.findUsersByKeyword(sigungu, title);
        for (NotificationKeywordUserDto taget : users) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            publisher.publishEvent(new KeywordNotiEvent(taget.getUser(), taget.getKeyword(), url, sigungu, title));
                        }
                    }
            );
        }
    }

    /**
     * 주소에서 마지막 상세주소만 빼고 동까지만 되도록 설정
     */
    private String getDong(String address) {
        int index = address.lastIndexOf(" ");
        return address.substring(0, index);
    }


}
