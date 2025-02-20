package com.guesthouse.assistpeople.entity;

import com.guesthouse.assistpeople.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.aspectj.bridge.Message;

import java.util.List;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long userId;

        @Column(unique = true,nullable = false)
        private String nickName;

        private String password,mbti,id,
                Introduce,reason,appeal,userPic;


        private String Role;

        @OneToMany(mappedBy = "messageId",cascade = CascadeType.ALL,orphanRemoval = true)
        private List<MessageEntity> messageEntities ;

        @OneToMany(mappedBy = "postId",cascade = CascadeType.ALL,orphanRemoval = true)
        private List<PostEntity> postEntities;



        public static UserEntity toVO(UserDTO memberDto) {
                return UserEntity.builder()
                        .id(memberDto.getId())
                        .nickName(memberDto.getUserName())
                        .password(memberDto.getPassword())
                        .build();
        }

}
