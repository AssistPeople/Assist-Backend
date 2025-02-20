package com.guesthouse.assistpeople.controller;

import com.guesthouse.assistpeople.dto.LoginDTO;
import com.guesthouse.assistpeople.dto.UserDTO;
import com.guesthouse.assistpeople.dto.UserInfoDTO;
import com.guesthouse.assistpeople.exeption.ApiResponse;
import com.guesthouse.assistpeople.jwt.CustomUserDetail;
import com.guesthouse.assistpeople.jwt.JwtToken;
import com.guesthouse.assistpeople.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
@Slf4j
public class UserController {
    private final UserService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDTO memberDto) {
        try {
            memberService.signUp(memberDto);
            return ResponseEntity.ok(new ApiResponse(true, "회원가입 성공", null));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "서버 오류가 발생했습니다.", null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) {
        try {
            JwtToken token = memberService.login(loginDto);
            return ResponseEntity.ok(new ApiResponse(true, "로그인 성공", token.getAccessToken()));
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, ex.getMessage(), null));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "서버 오류가 발생했습니다.", null));
        }
    }

    @GetMapping("/user-info")
    public ResponseEntity<ApiResponse> getUserInfo(@AuthenticationPrincipal CustomUserDetail customUserDetails) {
        try {
            log.info("ㅈㅂ : " + customUserDetails.getUsername());
            UserInfoDTO result = memberService.getUserInfo(customUserDetails.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "유저 정보 조회 성공", result));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "유저 정보 확인 중 에러가 발생했습니다\n." + e.getMessage(), null));
        }
    }

    @PutMapping("/update-user-info")
    public ResponseEntity<ApiResponse> updateUserInfo(@AuthenticationPrincipal CustomUserDetail customUserDetails,@RequestBody UserInfoDTO userInfoDTO) {
        try {

            userInfoDTO.setId(customUserDetails.getUsername());

            memberService.updateUserInfo(userInfoDTO);

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "유저 정보 수정 성공", null));


        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "유저 정보 확인 중 에러가 발생했습니다\n." + e.getMessage(), null));
        }
    }


}

