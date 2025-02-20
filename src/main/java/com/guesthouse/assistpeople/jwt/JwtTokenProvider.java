package com.guesthouse.assistpeople.jwt;

import com.guesthouse.assistpeople.dto.UserDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
@Component

public class JwtTokenProvider {
    private final Key key;



    public JwtTokenProvider(
            @Value("${spring.jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    public JwtToken generateToken(Authentication authentication) {
        try {
            if (authentication == null) {
                log.error("Authentication object is null");
                return null;  // 또는 적절한 처리를 해주세요
            }

            log.info("authentication1 : " + authentication);
            log.info("authentication.getPrincipal() : " + authentication.getPrincipal());

            // authentication.getPrincipal()이 CustomUserDetail인지를 확인
            if (!(authentication.getPrincipal() instanceof CustomUserDetail)) {
                log.error("Principal is not of type CustomUserDetail");
                return null; // 또는 예외를 던질 수 있습니다.
            }
            // CustomUserDetail에서 UserDTO 객체를 가져오는 방법
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            UserDTO member = customUserDetail.getMember();  // CustomUserDetail에서 UserDTO 가져오기

            log.info("Member password: " + member.getPassword());

            long now = (new Date()).getTime();

            // Access Token 생성
            Date accessTokenExpiresIn = new Date(now + 86400000); // 1일 후 만료
            String accessToken = Jwts.builder()
                    .setSubject(authentication.getName())
                    .claim("auth", authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(",")))
                    .setExpiration(accessTokenExpiresIn)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            // Refresh Token 생성
            String refreshToken = Jwts.builder()
                    .setExpiration(new Date(now + 86400000)) // 1일 후 만료
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            log.info("Access Token: " + accessToken);
            log.info("Refresh Token: " + refreshToken);

            return JwtToken.builder()
                    .grantType("Bearer")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (Exception e) {
            log.error("Error generating token", e);  // 예외 로그를 찍어주세요
            throw new RuntimeException("Error generating token", e);  // 예외를 던져서 상위에서 처리
        }
    }


    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {

        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }


        // 클레임에서 role 가져오기 (null 처리)
        String role = claims.get("role") != null ? claims.get("role").toString() : "ROLE_USER"; // 기본값 설정


        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());



        // UserDTO 객체 생성 (claims.getSubject()를 사용하여 UserDTO를 만들어야 함)
        UserDTO userDTO = new UserDTO();
        userDTO.setId(claims.getSubject());
        userDTO.setRole(role);


        // UserDetails 객체를 만들어서 Authentication return
        // UserDetails: interface, User: UserDetails를 구현한 class
        // CustomUserDetail 객체 생성 (UserDetails를 구현한 커스텀 클래스)

        UserDetails principal = new CustomUserDetail(userDTO);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}