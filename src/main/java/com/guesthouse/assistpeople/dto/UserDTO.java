package com.guesthouse.assistpeople.dto;

import com.guesthouse.assistpeople.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
        private Long userId;
        @NotBlank(message = "이름을 입력해주세요")
        private String userName;
        @NotBlank(message = "아이디를 입력해주세요")
        private String id;
        @NotBlank(message = "비밀번호를 입력해주세요")
        private String password;
        private String Role;



        public static UserDTO toDto(UserEntity member) {
            return new UserDTO(
                    member.getUserId(),
                    member.getNickName(),
                    member.getId(),
                    member.getPassword(),
                    member.getRole()
            );
        }
}


