package techit.gongsimchae.domain.portion.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.portion.chatroom.repository.ChatRoomRepository;
import techit.gongsimchae.domain.portion.feedback.repository.FeedbackRepository;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ChatRoomRepository chatRoomRepository;




}
