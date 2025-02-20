package com.guesthouse.assistpeople.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserInfoDTO {
    private Long userId;
    private String introduce;
    private String appeal;
    private String mbit;
    private String nickName;
    private String reason;
    private String userPic;
    private String id;
}
