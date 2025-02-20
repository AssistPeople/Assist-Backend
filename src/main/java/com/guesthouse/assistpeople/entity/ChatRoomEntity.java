package com.guesthouse.assistpeople.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat_room")
public class ChatRoomEntity {

        //채팅방 id(PK)
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long roomId;

        //채팅방 이름
        @Column(length = 100, nullable = false)
        private String roomName;

        //생성 날짜
        @Temporal(TemporalType.TIMESTAMP)
        private Date createDate;


        @OneToMany(mappedBy = "messageId",cascade = CascadeType.ALL,orphanRemoval = true)
        private List<MessageEntity> messageEntities;

        @OneToMany(mappedBy = "participateId",cascade = CascadeType.ALL,orphanRemoval = true)
        private List<PerticipateEntity>perticipateEntities ;



}
