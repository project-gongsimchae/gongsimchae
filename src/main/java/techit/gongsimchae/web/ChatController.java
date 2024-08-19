package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import techit.gongsimchae.domain.portion.chatmessage.dto.ChatMessageDto;
import techit.gongsimchae.domain.portion.chatmessage.service.ChatMessageService;
import techit.gongsimchae.domain.portion.chatroom.service.ChatRoomService;
import techit.gongsimchae.global.dto.PrincipalDetails;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;


    // 채팅방 입장 화면
    // 파라미터로 넘어오는 roomId 를 확인후 해당 roomId 를 기준으로
    // 채팅방을 찾아서 클라이언트를 chatroom 으로 보낸다.
    @GetMapping("/chat/room")
    public String roomDetail(Model model, @RequestParam("roomId") String roomId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        log.info("roomId {}", roomId);

        // 세션에서 로그인 유저 정보를 가져옴
        model.addAttribute("user", principalDetails.getAccountDto());

        model.addAttribute("room", chatRoomService.getChatRoom(roomId));
        log.info("rome detail {}", chatRoomService.getChatRoom(roomId));
        return "portion/chatroom";
    }

    /**
     * 이전 대화들을 본다
     */
    @GetMapping("/chat/previous/{roomId}")
    @ResponseBody
    public ResponseEntity<List<ChatMessageDto>> getPrevious(@PathVariable("roomId") String roomId) {
        List<ChatMessageDto> messages = chatMessageService.getMessages(roomId);
        log.debug("previous messages : {}", messages);
        return ResponseEntity.ok().body(messages);
    }

    @GetMapping("/chat/chkUserCnt/{roomId}")
    @ResponseBody
    public boolean chUserCnt(@PathVariable("roomId") String roomId) {

        return chatRoomService.checkRoomUserCount(roomId);
    }
}
