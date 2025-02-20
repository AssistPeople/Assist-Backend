package com.guesthouse.assistpeople.repository;

import com.guesthouse.assistpeople.entity.ChatRoomEntity;
import com.guesthouse.assistpeople.entity.PerticipateEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    ChatRoomEntity findByRoomId(Long roomId);
//    ChatRoomEntity findByRoomIdAndRoomType(Long roomId, Long roomType);
    // 비관적락 걸기
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c " +
            "from ChatRoomEntity  c " +
            "where c.roomName = CONCAT(:nickname1, :nickname2) or c.roomName = CONCAT(:nickname2, :nickname1)")
    Optional<ChatRoomEntity> findByRoomName(@Param("nickname1") String nickname1, @Param("nickname2") String nickname2);
}
