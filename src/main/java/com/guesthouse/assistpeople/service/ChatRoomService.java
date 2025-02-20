package com.guesthouse.assistpeople.service;

import com.guesthouse.assistpeople.dto.ChatRoomDTO;
import com.guesthouse.assistpeople.dto.ChatRoomWithMessageDTO;
import com.guesthouse.assistpeople.entity.*;
import com.guesthouse.assistpeople.jwt.CustomUserDetail;
import com.guesthouse.assistpeople.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ParticipateRepository participateRepository;
    private final MessageRepository messageRepository;

    // 채팅방 만들어주는 로직
    @Transactional
    public ChatRoomDTO createChatRoom(Long postId,@AuthenticationPrincipal CustomUserDetail customUserDetails) {

        //user1 찾기
        UserEntity user1 = userRepository.findById(customUserDetails.getUsername());

        //user2 찾기
        PostEntity post = postRepository.findByPostId(postId);
        UserEntity user2 = userRepository.findById(post.getUser().getId());

        // 동시성 제어->동일한 채팅방 생기지 않게 , user1 은 로그인된 유저, user2는 post 올린 유저 가져오기
        Optional<ChatRoomEntity> existingRoom = chatRoomRepository.findByRoomName(user1.getNickName(), user2.getNickName());
        if (existingRoom.isPresent()) {
            return convertDTO(existingRoom.get());
        }
        //채팅방 만들기
        ChatRoomEntity chatRoom = chatRoomRepository.save(ChatRoomEntity.builder()
                .createDate(new Date())
                .roomName(user1.getNickName() + user2.getNickName())
                .build());

        // participateEntity에 추가하기 (참가자 추가)
        participateRepository.findByUserAndRoomId(user1,chatRoom).orElseGet(()->
                participateRepository.save(PerticipateEntity.builder()
                        .roomId(chatRoom)
                        .userId(user1)
                        .build())
        );

        participateRepository.findByUserAndRoomId(user2, chatRoom).orElseGet(() ->
                participateRepository.save(PerticipateEntity.builder()
                        .roomId(chatRoom)
                        .userId(user2)
                        .build())
        );

        return convertDTO(chatRoom);
    }
    public ChatRoomDTO convertDTO(ChatRoomEntity chatRoom) {
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO.setRoomId(chatRoom.getRoomId());
        return chatRoomDTO;
    }

    // 채팅방 상세 보기
    public List<MessageEntity> roomDetail(Long roomId){
        ChatRoomEntity chatRoom = chatRoomRepository.findByRoomId(roomId);
        return messageRepository.findByRoomId(chatRoom);
    }

    // 채팅방 목록 반환
//    public List<ChatRoomWithMessageDTO> getMatchRooms(@AuthenticationPrincipal CustomUserDetail customUserDetails){
//        UserEntity user = userRepository.findByStudentId_StudentId(principalDetails.getUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
//
//        List<participateEntity> getAllRooms = participateRepository.findByUserId(user); // 로그인한 사용자의 모든 room 찾기
//
//        List<ChatRoomWithMessageDTO> matchRooms = new ArrayList<>();
//        for(int i = 0; i <getAllRooms.size(); i++){
//            chatRoomEntity matchRoom = chatRoomRepository.findByRoomId(getAllRooms.get(i).getRoomId().getRoomId());
//            // roomType이 0(1대1 매칭)인 채팅방 찾아서 list에 넣기
//            if(matchRoom.getRoomType()==0){
//                Optional<messageEntity> lastMessage = messageRepository.findFirstByRoomIdOrderBySendTime(matchRoom);
//                ChatRoomWithMessageDTO.ChatRoomWithMessageDTOBuilder dtoBuilder = ChatRoomWithMessageDTO.builder()
//                        .roomId(getAllRooms.get(i).getRoomId().getRoomId())
//                        .roomName(getAllRooms.get(i).getRoomId().getRoomName())
//                        .roomType(matchRoom.getRoomType());
//
//                // 마지막 메시지가 존재하는지 체크
//                if (lastMessage.isPresent()) {
//                    // 메시지가 존재할 경우
//                    dtoBuilder
//                            .messageContent(lastMessage.get().getMessageContent())
//                            .mesaageTimestamp(lastMessage.get().getSendTime());
//                } else {
//                    // 메시지가 없을 경우 (null 처리)
//                    dtoBuilder
//                            .messageContent(null)
//                            .mesaageTimestamp(null);
//                }
//
//                // DTO 객체 생성 후 리스트에 추가
//                matchRooms.add(dtoBuilder.build());
//            }
//        }
//        return matchRooms;
//    }


}
