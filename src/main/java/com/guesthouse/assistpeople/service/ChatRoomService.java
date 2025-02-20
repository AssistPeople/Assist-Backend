package com.guesthouse.assistpeople.service;

import com.guesthouse.assistpeople.dto.ChatRoomDTO;
import com.guesthouse.assistpeople.entity.ChatRoomEntity;
import com.guesthouse.assistpeople.entity.PerticipateEntity;
import com.guesthouse.assistpeople.entity.PostEntity;
import com.guesthouse.assistpeople.entity.UserEntity;
import com.guesthouse.assistpeople.jwt.CustomUserDetail;
import com.guesthouse.assistpeople.repository.ChatRoomRepository;
import com.guesthouse.assistpeople.repository.ParticipateRepository;
import com.guesthouse.assistpeople.repository.PostRepository;
import com.guesthouse.assistpeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ParticipateRepository participateRepository;
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


}
