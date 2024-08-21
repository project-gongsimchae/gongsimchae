package techit.gongsimchae.domain.portion.notificationkeyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.portion.notificationkeyword.dto.NotiKeywordCreateDtoWeb;
import techit.gongsimchae.domain.portion.notificationkeyword.entity.NotificationKeyword;
import techit.gongsimchae.domain.portion.notificationkeyword.repository.NotiKeywordRepository;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotiKeywordService {

    private final NotiKeywordRepository notiKeywordRepository;
    private final UserRepository userRepository;


    @Transactional
    public Long createKeyword(NotiKeywordCreateDtoWeb createDto, PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException(ErrorMessage.USER_NOT_FOUND));
        NotificationKeyword notificationKeyword = new NotificationKeyword(createDto.getNotiKeword(), user);
        NotificationKeyword savedKeyword = notiKeywordRepository.save(notificationKeyword);
        return savedKeyword.getId();
    }



}
