package com.guesthouse.assistpeople.repository;

import com.guesthouse.assistpeople.entity.ChatRoomEntity;
import com.guesthouse.assistpeople.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findByRoomId(ChatRoomEntity chatRoom);
    Optional<MessageEntity> findFirstByRoomIdOrderBySendTime(ChatRoomEntity roomId);
}
