package techit.gongsimchae.domain.portion.subdivision.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.imagefile.repository.ImageFileRepository;
import techit.gongsimchae.domain.common.imagefile.service.ImageS3Service;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.portion.chatroom.service.ChatRoomService;
import techit.gongsimchae.domain.portion.notifications.dto.NotificationKeywordUserDto;
import techit.gongsimchae.domain.portion.notifications.event.KeywordNotiEvent;
import techit.gongsimchae.domain.portion.subdivision.dto.*;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;
import techit.gongsimchae.domain.portion.subdivision.entity.SubdivisionType;
import techit.gongsimchae.domain.portion.subdivision.repository.SubdivisionRepository;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;
import techit.gongsimchae.global.util.ViewVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * 소분글 메인페이지에 모든 소분글들을 보여주는 메서드
     */
    public List<SubdivisionRespDto> getAllSubdivisions(){
        List<Subdivision> subdivisions = subdivisionRepository.findByDeleteStatusIsFalseOrderByCreateDateDesc();
        return subdivisions.stream()
                .map(SubdivisionRespDto::new)
                .collect(Collectors.toList());
    }

    /**
     * URL의 Path 값으로 넘어온 UID로 DB에서 해당 소분 글을 찾아주는 메서드
     *
     */
    public SubdivisionRespDto findSubdivisionByUID(String UID) {

        Subdivision subdivision = subdivisionRepository.findByUID(UID).orElseThrow(() -> new CustomWebException("해당 소분 글을 찾을 수 없습니다."));

        return new SubdivisionRespDto(subdivision);

    }

    /**
     * UserId를 기반으로 자신이 작성한 소분 글 찾아주는 메서드
     *
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
                .build();

        Subdivision savedSubdivision = subdivisionRepository.save(subdivision);

        notifyKeyword(savedSubdivision.getSigungu(), savedSubdivision.getTitle(),savedSubdivision.getUID());

        // chatroom 생성
        chatRoomService.create(savedSubdivision);

        imageS3Service.storeFiles(subdivisionReqDto.getImages(), "images", subdivision);

        return subdivision.getUID();
    }


    @Transactional
    public void viewCountValidation(String UID, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(ViewVO.SUBDIVISIONVIEW))
                .findFirst().orElseGet(() -> {
                    addViews(UID);
                    return new Cookie(ViewVO.SUBDIVISIONVIEW, UID);
                });

        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        cookie.setPath("/");
        cookie.setMaxAge((int) (todayEndSecond - currentSecond));
        response.addCookie(cookie);
    }

    private void addViews(String UID) {
        Subdivision subdivision = subdivisionRepository.findByUID(UID).orElseThrow(() -> new CustomWebException(ErrorMessage.SUBDIVISION_NOT_FOUND));
        subdivision.addView();
    }

    /**
     * 소분글을 수정하는 메서드
     */
    @Transactional
    public String updateSubdivision(SubdivisionUpdateReqDto subdivisionUpdateReqDto) {

        log.debug("subdivisionUpdateReqDto {}", subdivisionUpdateReqDto);

        Subdivision subdivision = subdivisionRepository.findById(subdivisionUpdateReqDto.getId()).orElseThrow(() -> new CustomWebException("Subdivision not found"));
        subdivision.updateSubdivision(subdivisionUpdateReqDto);

        notifyKeyword(subdivision.getSigungu(), subdivision.getTitle(),subdivision.getUID());

        imageS3Service.storeFiles(subdivisionUpdateReqDto.getImages(), "images", subdivision);

        if (!Objects.isNull(subdivisionUpdateReqDto.getDeleteImages())) {
            for (Long deleteImageId : subdivisionUpdateReqDto.getDeleteImages()) {

                ImageFile imageFile = imageFileRepository.findById(deleteImageId).orElseThrow(() -> new CustomWebException("ImageFile not found"));

                imageS3Service.deleteFile(imageFile.getStoreFilename());
                imageS3Service.deleteFileFromDb(imageFile);
            }
        }

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
    }

    /**
     * 소분글 메인페이지에서 주소와 내용을 가지고 검색을 하는 메서드
     */
    public List<SubdivisionRespDto> searchSubdivisions(String address, String content) {
        // 콤마(,)로 구분된 주소를 여러 개로 나누기
        String[] addresses = address != null ? address.split(",") : new String[]{};

        List<Subdivision> results = new ArrayList<>();

        for (String addr : addresses) {
            // 각 주소에 대해 쿼리 실행
            List<Subdivision> partialResults = subdivisionRepository.searchSubdivisions(addr.trim(), content);
            results.addAll(partialResults);
        }

        // 중복된 결과를 제거 (필요시)
        results = results.stream().distinct().collect(Collectors.toList());

        return results.stream()
                .map(SubdivisionRespDto::new)
                .collect(Collectors.toList());
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
                            publisher.publishEvent(new KeywordNotiEvent(taget.getUser(),taget.getKeyword(),url,sigungu,title));
                        }
                    }
            );
        }
    }


}
