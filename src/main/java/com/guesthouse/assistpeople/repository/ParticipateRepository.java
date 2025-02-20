package com.guesthouse.assistpeople.repository;

import com.guesthouse.assistpeople.entity.ChatRoomEntity;
import com.guesthouse.assistpeople.entity.PerticipateEntity;
import com.guesthouse.assistpeople.entity.UserEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipateRepository extends JpaRepository<PerticipateEntity, Long> {

    List<PerticipateEntity> findByUserId(UserEntity user);
    List<PerticipateEntity> findByRoomId(ChatRoomEntity user);
    // participate 테이블에서 로그인한 사용자의 userId와 입장해 있는 roomId가 같은 거 찾아서 삭제
    @Modifying
    @Query("DELETE FROM PerticipateEntity p WHERE p.userId.userId = :userId AND p.roomId.roomId = :roomId")
    void deleteByUserIdAndRoomId(@Param("userId") Long userId, @Param("roomId") Long roomId);
    // 비관적락 걸기
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p " +
            "from PerticipateEntity p " +
            "where p.userId = :userId AND p.roomId = :roomId")
    Optional<PerticipateEntity> findByUserAndRoomId(@Param("userId") UserEntity userId, @Param("roomId") ChatRoomEntity roomId);

}
