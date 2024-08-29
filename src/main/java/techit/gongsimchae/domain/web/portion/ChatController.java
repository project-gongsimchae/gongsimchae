package techit.gongsimchae.domain.web.portion;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import techit.gongsimchae.domain.common.imagefile.dto.ImageFileRespDto;
import techit.gongsimchae.domain.common.user.dto.UserRespDtoWeb;
import techit.gongsimchae.domain.common.user.service.UserService;
import techit.gongsimchae.domain.portion.chatmessage.dto.ChatMessageDto;
import techit.gongsimchae.domain.portion.chatmessage.service.ChatMessageService;
import techit.gongsimchae.domain.portion.chatmessage.service.MessageSenderService;
import techit.gongsimchae.domain.portion.chatroom.dto.ChatRoomRespDto;
import techit.gongsimchae.domain.portion.chatroom.service.ChatRoomService;
import techit.gongsimchae.domain.portion.chatroomuser.service.ChatRoomUserService;
import techit.gongsimchae.global.dto.PrincipalDetails;

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
    public ResponseEntity<?> chUserCnt(@PathVariable("roomId") String roomId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        boolean result = chatRoomService.checkRoomUserCount(roomId, principalDetails);
        if (result) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload ChatMessageDto chat, SimpMessageHeaderAccessor headerAccessor) {
        String name = chat.getSender();
        UserRespDtoWeb user = userService.getUserByNickname(name);


        log.debug("enter User {}", chat);


        // 반환 결과를 socket session 에 userUUID 로 저장
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());
        headerAccessor.getSessionAttributes().put("loginId", chat.getLoginId());

        senderService.send(chat);

    }

    // 해당 유저
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatMessageDto chat) {
        log.info("send message chat {}", chat);
        senderService.send(chat);

    }

    // 채팅에 참여한 유저 리스트 반환
    @GetMapping("/chat/userlist")
    @ResponseBody
    public List<String> userList(String roomId) {
        return chatRoomService.getUserList(roomId);
    }

    // ai 채팅
    @GetMapping("/chat/ai")
    public String chatRoom(Model model) {
        String name = "user";
        ChatRoomRespDto chatRoomRespDto = new ChatRoomRespDto(UUID.randomUUID().toString().substring(0,8),"AI");
        model.addAttribute("room", chatRoomRespDto);
        model.addAttribute("user", name);
        return "ai";
    }


    @MessageMapping("/chat/ai/sendMessage")
    public void AISendMessage(@Payload ChatMessageDto chatMessageDto) {
        log.debug("chat message: {}", chatMessageDto);
        senderService.sendToAI(chatMessageDto);
    }

    @PostMapping("/chat/s3/upload")
    @ResponseBody
    public ImageFileRespDto uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("roomId") String roomId) {
        ImageFileRespDto imageFileRespDto = chatRoomService.uploadPhotoToChat(roomId, file);
        log.debug("imageFileRespDto {}", imageFileRespDto);
        return imageFileRespDto;
    }


    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("DisConnEvent {}", event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());


        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        String loginId = (String) headerAccessor.getSessionAttributes().get("loginId");

        if (chatRoomUserService.isUserAlreadyInRoom(roomId, loginId)) {
            chatRoomUserService.disableChatRoomOnLeave(roomId, loginId);
        }


    }


}
