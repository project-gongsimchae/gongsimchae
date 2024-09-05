package techit.gongsimchae.domain.common.participate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.participate.entity.Participate;
import techit.gongsimchae.domain.common.participate.repository.ParticipateRepository;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.groupbuying.itemoption.entity.ItemOption;
import techit.gongsimchae.domain.groupbuying.itemoption.repository.ItemOptionRepository;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipateService {

    private final ParticipateRepository participateRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createParticipate(Long itemOptionId, String loginId) {
        ItemOption itemOption = itemOptionRepository.fetchItemByOptionId(itemOptionId).orElseThrow(() -> new CustomWebException(ErrorMessage.USER_NOT_FOUND));
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomWebException(ErrorMessage.USER_NOT_FOUND));

        Participate savedParticipate = participateRepository.save(new Participate(user, itemOption.getItem()));
        return savedParticipate.getId();
    }

    public Long getParticipateCount(Long itemId) {
        return participateRepository.countByItem(itemId);
    }

    /**
     * 참여한 인원 삭제하는 로직
     * @param itemId
     * @param loginId
     */
    @Transactional
    public void removeParticipate(Long itemId, String loginId) {
        Optional<Participate> _participate = participateRepository.findParticipate(itemId, loginId);
        _participate.ifPresent(participateRepository::delete);
    }
}
