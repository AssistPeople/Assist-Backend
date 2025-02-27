package com.guesthouse.assistpeople.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "message")
public class MessageEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long messageId;

        //메시지 내용
        @Column(columnDefinition = "TEXT")
        private String messageContent;

        // 보낸 시간
        @Temporal(TemporalType.TIMESTAMP)
        private Date sendTime;

        // 유저(FK)
        @ManyToOne
        @JoinColumn(name = "userId", nullable = false)
        private UserEntity userId;

        // 채팅방 id(FK)
        @ManyToOne
        @JoinColumn(name = "roomId", nullable = false)
        private ChatRoomEntity roomId;

}
