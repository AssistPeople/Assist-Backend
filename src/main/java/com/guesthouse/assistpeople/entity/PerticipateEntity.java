package com.guesthouse.assistpeople.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "participate")
public class PerticipateEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long participateId;
        // 유저(FK)
        @ManyToOne
        @JoinColumn(name = "userId", nullable = false)
        private UserEntity userId;

        // 채팅방 id
        @ManyToOne
        @JoinColumn(name = "roomId")
        private ChatRoomEntity roomId;

}
