package techit.gongsimchae.domain.portion.feedback.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.portion.feedback.dto.FeedbackReqDtoWeb;
import techit.gongsimchae.domain.portion.feedback.dto.FeedbackUserRespDtoWeb;
import techit.gongsimchae.domain.portion.feedback.entity.Feedback;
import techit.gongsimchae.domain.portion.feedback.repository.FeedbackRepository;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RedisTemplate<String, Object> redisTemplate;


    public List<FeedbackUserRespDtoWeb> findUsersForFeedback(String url, PrincipalDetails principalDetails) {
        String json = (String) redisTemplate.opsForValue().get(url);

        try{
            List<FeedbackUserRespDtoWeb> users = objectMapper.readValue(json, new TypeReference<List<FeedbackUserRespDtoWeb>>() {});
            return users.stream().filter(u -> !u.getId().equals(principalDetails.getAccountDto().getId()))
                    .toList();

        } catch (Exception e) {
            throw new CustomWebException(e);
        }

    }

    public Long createFeedback(FeedbackReqDtoWeb reqDtoWeb) {
        User user = userRepository.findById(reqDtoWeb.getUserId()).orElseThrow(() -> new CustomWebException(ErrorMessage.USER_NOT_FOUND));
        Feedback feedback = new Feedback(reqDtoWeb, user);
        Feedback savedFeedback = feedbackRepository.save(feedback);
        return savedFeedback.getId();
    }
}
