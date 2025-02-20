package com.guesthouse.assistpeople.service;

import com.guesthouse.assistpeople.dto.LoginDTO;
import com.guesthouse.assistpeople.dto.UserDTO;
import com.guesthouse.assistpeople.dto.UserInfoDTO;
import com.guesthouse.assistpeople.entity.UserEntity;
import com.guesthouse.assistpeople.jwt.CustomUserDetail;
import com.guesthouse.assistpeople.jwt.JwtToken;
import com.guesthouse.assistpeople.jwt.JwtTokenProvider;
import com.guesthouse.assistpeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

        private final UserRepository userRepository;
        private final BCryptPasswordEncoder bCryptPasswordEncoder;
        private final JwtTokenProvider jwtTokenProvider;

        @Transactional
        public void signUp(UserDTO memberDto) {
            if(userRepository.findById(memberDto.getId())!= null){
                throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
            }

            memberDto.setPassword(bCryptPasswordEncoder.encode(memberDto.getPassword()));
            UserEntity vo = UserEntity.toVO(memberDto);
            userRepository.save(vo);

        }
        @Transactional
        public JwtToken login(LoginDTO loginDto) {
            log.info("findById() 호출: " + loginDto.getId());
            UserEntity member = userRepository.findById(loginDto.getId());

            if(member == null){
                throw new UsernameNotFoundException("존재하지 않는 아이디입니다.");
            }
            if(!bCryptPasswordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
                throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
            }
            log.info("findById() 호출: " + loginDto.getId());
            UserDTO userDTO = UserDTO.builder()
                    .Role(member.getRole())
                    .id(loginDto.getId())
                    .password(loginDto.getPassword()).build();

            // CustomUserDetail 생성
            CustomUserDetail customUserDetail = new CustomUserDetail(userDTO);

            // Authentication 객체 생성 (UsernamePasswordAuthenticationToken 사용)
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    customUserDetail,
                    null,
                    customUserDetail.getAuthorities()
            );

            log.info("authentication : "+  authentication.getName());


            return jwtTokenProvider.generateToken(authentication);

        }

        public UserInfoDTO getUserInfo(String id){
            UserEntity user = userRepository.findById(id);
            log.info("user : "+user.getUserId());
            if (user == null) {
                throw new UsernameNotFoundException("존재하지 않는 유저입니다.");
            }
            UserInfoDTO userInfoDTO = UserInfoDTO.builder()
                    .userPic(user.getUserPic())
                    .userId(user.getUserId())
                    .introduce(user.getIntroduce())
                    .appeal(user.getAppeal())
                    .mbit(user.getMbti())
                    .nickName(user.getNickName())
                    .reason(user.getReason())
                    .build();
            return userInfoDTO;

        }

    public void updateUserInfo(UserInfoDTO userInfoDTO ){
        userRepository.updateUser(
                userInfoDTO.getNickName(),
                userInfoDTO.getMbit(),
                userInfoDTO.getIntroduce(),
                userInfoDTO.getReason(),
                userInfoDTO.getAppeal(),
                userInfoDTO.getUserPic(),
                userInfoDTO.getId()
        );
    }



    }


