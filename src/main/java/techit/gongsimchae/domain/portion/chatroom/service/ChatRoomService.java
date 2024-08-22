package techit.gongsimchae.domain.portion.chatroom.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import techit.gongsimchae.domain.common.imagefile.dto.ImageFileRespDto;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.imagefile.entity.S3VO;
import techit.gongsimchae.domain.common.imagefile.service.ImageS3Service;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.portion.chatroom.dto.ChatRoomRespDto;
import techit.gongsimchae.domain.portion.chatroom.entity.ChatRoom;
import techit.gongsimchae.domain.portion.chatroom.repository.ChatRoomRepository;
import techit.gongsimchae.domain.portion.chatroomuser.entity.ChatRoomUser;
import techit.gongsimchae.domain.portion.chatroomuser.repository.ChatRoomUserRepository;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ImageS3Service imageS3Service;

    /**
     * 채팅방 만드는 메서드
     */
    @Transactional
    public void create(Subdivision subdivision) {
        ChatRoom chatRoom = new ChatRoom(subdivision);
        chatRoomRepository.save(chatRoom);
    }

    /**
     * roomId를 기준으로 채팅방을 찾는 메서드
     */
    public ChatRoomRespDto getChatRoom(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        return new ChatRoomRespDto(chatRoom);
    }

    /**
     * subdivision ID를 토대로 채팅방을 찾는 메서드
     */

    public ChatRoomRespDto getChatRoom(Long subdivisionId) {
        ChatRoom chatRoom = chatRoomRepository.findBySubdivisionId(subdivisionId).orElseThrow(() -> new CustomWebException(ErrorMessage.CHATTING_ROOM_NOT_FOUND));
        return new ChatRoomRespDto(chatRoom);
    }

    /**
     * 채팅방에 유저 넣고 빼기
     */
    @Transactional
    public void updateUserInChat(String roomId, String nickname) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(() -> new CustomWebException(ErrorMessage.CHATTING_ROOM_NOT_FOUND));
        User user = userRepository.findByNickname(nickname).orElseThrow(() -> new CustomWebException(ErrorMessage.USER_NOT_FOUND));
        Optional<ChatRoomUser> _chatRoomUser = chatRoomUserRepository.findByChatUser(roomId, nickname);
        if(_chatRoomUser.isPresent()) {
            ChatRoomUser chatRoomUser = _chatRoomUser.get();
            chatRoomUserRepository.delete(chatRoomUser);
        } else{
            ChatRoomUser chatRoomUser = new ChatRoomUser(user, chatRoom);
            chatRoomUserRepository.save(chatRoomUser);
        }

    }

    /**
     * maxUserCnt에 따른 채팅방 입장 여부
     */
    public boolean checkRoomUserCount(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(() -> new CustomWebException(ErrorMessage.CHATTING_ROOM_NOT_FOUND));
        int currentUserCount = chatRoom.getChatRoomUsers().size();
        if (currentUserCount + 1 > chatRoom.getMaxUserCnt()) {
            return false;
        }
        return true;
    }

    /**
     * 채팅방 전체 유저 조회
     */
    public ArrayList<String> getUserList(String roomId){
        ArrayList<String> list = new ArrayList<>();

        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(() -> new CustomWebException(ErrorMessage.CHATTING_ROOM_NOT_FOUND));
        chatRoom.getChatRoomUsers().forEach(i -> list.add(i.getUser().getName()));
        return list;
    }

    @Transactional
    public ImageFileRespDto uploadPhotoToChat(String roomId, MultipartFile file) {
        log.debug("uploadPhotoToChat {} ", file);
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(() -> new CustomWebException(ErrorMessage.CHATTING_ROOM_NOT_FOUND));
        ImageFile imageFile = imageS3Service.storeFile(file, S3VO.CHATTING_IMAGE_UPLOAD_DIRECTORY, chatRoom);
        return ImageFileRespDto.builder()
                .roomId(chatRoom.getRoomId())
                .originalFilename(imageFile.getOriginalFilename())
                .storeFilename(imageFile.getStoreFilename()).build();
    }
}
