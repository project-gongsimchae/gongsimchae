package techit.gongsimchae.domain.portion.notificationkeyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.portion.notificationkeyword.entity.NotificationKeyword;
import techit.gongsimchae.domain.portion.notificationkeyword.repository.NotiKeywordRepository;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotiKeywordService {

    private final NotiKeywordRepository notiKeywordRepository;
    private final UserRepository userRepository;


    @Transactional
    public Long createKeyword(String keyword, PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException(ErrorMessage.USER_NOT_FOUND));
        NotificationKeyword notificationKeyword = new NotificationKeyword(keyword, user);
        NotificationKeyword savedKeyword = notiKeywordRepository.save(notificationKeyword);
        return savedKeyword.getId();
    }


    public List<String> getNotificationKeywords(PrincipalDetails principalDetails) {
        return notiKeywordRepository.findAllByUserId(principalDetails.getAccountDto().getId())
                .stream().map(NotificationKeyword::getKeyword).collect(Collectors.toList());
    }
    @Transactional
    public void deleteNotificationKeyword(String keyword, PrincipalDetails principalDetails) {
        notiKeywordRepository.deleteKeywordByUser(keyword, principalDetails.getAccountDto().getId());
    }
}
