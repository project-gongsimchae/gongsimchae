package techit.gongsimchae.domain.portion.subdivision.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.imagefile.repository.ImageFileRepository;
import techit.gongsimchae.domain.common.imagefile.service.ImageS3Service;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.portion.participants.service.ParticipantService;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionReqDto;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionRespDto;
import techit.gongsimchae.domain.portion.subdivision.dto.SubdivisionUpdateReqDto;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;
import techit.gongsimchae.domain.portion.subdivision.repository.SubdivisionRepository;
import techit.gongsimchae.global.exception.CustomWebException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SubdivisionService {

    private final SubdivisionRepository subdivisionRepository;
    private final ParticipantService participantService;
    private final UserRepository userRepository;
    private final ImageS3Service imageS3Service;
    private final ImageFileRepository imageFileRepository;

    @Transactional(readOnly = true)
    public List<SubdivisionRespDto> getAllSubdivisions(){
        List<Subdivision> subdivisions = subdivisionRepository.findByOrderByCreateDateDesc();
        return subdivisions.stream()
                .map(SubdivisionRespDto::new)
                .collect(Collectors.toList());
    }

    /**
     * URL의 Path 값으로 넘어온 UID로 DB에서 해당 소분 글을 찾아주는 메서드
     *
     */
    @Transactional(readOnly = true)
    public SubdivisionRespDto findSubdivisionByUID(String UID) {

        Subdivision subdivision = subdivisionRepository.findByUID(UID).orElseThrow(() -> new CustomWebException("해당 소분 글을 찾을 수 없습니다."));

        return new SubdivisionRespDto(subdivision);

    }

    /**
     * UserId를 기반으로 자신이 작성한 소분 글 찾아주는 메서드
     *
     */
    @Transactional(readOnly = true)
    public List<SubdivisionRespDto> findSubdivisionByUserId(Long userId) {

        return subdivisionRepository.findAllByUserId(userId).stream().map(SubdivisionRespDto::new).toList();
    }

    /**
     * UserId를 기반으로 해당 유저가 참여중인 소분글 가져오는 메서드
     *
     */
    @Transactional(readOnly = true)
    public List<SubdivisionRespDto> findJoinSubdivisionByUserId(Long userId) {

        return participantService.findByUserId(userId).stream().map(participant -> participant.getSubdivision()).toList();
    }

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
                .user(user)
                .build();

        subdivisionRepository.save(subdivision);

        imageS3Service.storeFiles(subdivisionReqDto.getImages(), "images", userId, subdivision);

        return subdivision.getUID();
    }

    public String updateSubdivision(SubdivisionUpdateReqDto subdivisionUpdateReqDto) {

        Subdivision subdivision = subdivisionRepository.findById(subdivisionUpdateReqDto.getId()).orElseThrow(() -> new CustomWebException("Subdivision not found"));
        subdivision.updateSubdivision(subdivisionUpdateReqDto);

        imageS3Service.storeFiles(subdivisionUpdateReqDto.getImages(), "images", subdivision.getUser().getId(), subdivision);

        if (!Objects.isNull(subdivisionUpdateReqDto.getDeleteImages())) {
            for (Long deleteImageId : subdivisionUpdateReqDto.getDeleteImages()) {

                ImageFile imageFile = imageFileRepository.findById(deleteImageId).orElseThrow(() -> new CustomWebException("ImageFile not found"));

                imageS3Service.deleteFile(imageFile.getStoreFilename());
                imageS3Service.deleteFileFromDb(imageFile);
            }
        }

        return subdivision.getUID();
    }
}
