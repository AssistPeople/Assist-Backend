package com.guesthouse.assistpeople.jwt;

import com.guesthouse.assistpeople.dto.UserDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
@Getter
@RequiredArgsConstructor
public class CustomUserDetail implements UserDetails{


        private final UserDTO member;


        // UserDTO 반환
        public UserDTO getMember() {
            return member;
        }

        // role 가져오기
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singleton(new SimpleGrantedAuthority(member.getRole()));
        }
        //이름
        public String getName() {
            return member.getUserName();
        }
        //패스워드
        @Override
        public String getPassword() {
            return member.getPassword();
        }

        //유저 id (pk 아님)
        @Override
        public String getUsername() {
            return member.getId();
        }



        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }


    }




