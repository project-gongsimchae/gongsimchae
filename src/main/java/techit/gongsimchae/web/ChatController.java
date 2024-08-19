package techit.gongsimchae.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.common.user.service.UserService;
import techit.gongsimchae.domain.portion.chatmessage.dto.ChatMessageDto;
import techit.gongsimchae.domain.portion.chatmessage.service.ChatMessageService;
import techit.gongsimchae.domain.portion.chatroom.dto.ChatRoomRespDto;
import techit.gongsimchae.domain.portion.chatroom.service.ChatRoomService;
import techit.gongsimchae.domain.portion.chatmessage.service.MessageSenderService;
import techit.gongsimchae.domain.portion.chatroomuser.entity.ChatRoomUser;
import techit.gongsimchae.domain.portion.chatroomuser.service.ChatRoomUserService;
import techit.gongsimchae.global.dto.PrincipalDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final MessageSenderService senderService;
    private final UserService userService;
    private final ChatRoomUserService chatRoomUserService;


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

    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload ChatMessageDto chat, SimpMessageHeaderAccessor headerAccessor) {
        String name = chat.getSender();
        UserRespDtoWeb user = userService.getUserByNickname(name);


        log.debug("enter User {}", chat);


        // 반환 결과를 socket session 에 userUUID 로 저장
        headerAccessor.getSessionAttributes().put("userUUID", user.getUID());
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());
        headerAccessor.getSessionAttributes().put("nickname", chat.getSender());

        senderService.send(chat);

    }

    // 해당 유저
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatMessageDto chat) {
        log.info("CHAT {}", chat);
        senderService.send(chat);

    }

    // 채팅에 참여한 유저 리스트 반환
    @GetMapping("/chat/userlist")
    @ResponseBody
    public ArrayList<String> userList(String roomId) {

        return chatRoomService.getUserList(roomId);
    }

    // ai 채팅
    @GetMapping("/chat/ai")
    public String chatRoom(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
        String name = principal.getUsername();
        String roomId = UUID.randomUUID().toString().substring(0, 8) + name;
        ChatRoomRespDto chatRoomRespDto = new ChatRoomRespDto();
        chatRoomRespDto.setRoomId(roomId);
        chatRoomRespDto.setRoomName(name);
        model.addAttribute("room", chatRoomRespDto);
        model.addAttribute("user", principal.getAccountDto());
        return "ai";
    }


    @MessageMapping("/chat/ai/sendMessage")
    public void AISendMessage(@Payload ChatMessageDto chatMessageDto) {
        log.debug("chat message: {}", chatMessageDto);
        senderService.sendToAI(chatMessageDto);
    }

    // 유저 퇴장 시에는 EventListener 을 통해서 유저 퇴장을 확인
    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("DisConnEvent {}", event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // stomp 세션에 있던 uuid 와 roomId 를 확인해서 채팅방 유저 리스트와 room 에서 해당 유저를 삭제
        String userUUID = (String) headerAccessor.getSessionAttributes().get("userUUID");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        String nickname = (String) headerAccessor.getSessionAttributes().get("nickname");

        chatRoomUserService.disableChatRoomOnLeave(roomId, nickname);

    }


}
