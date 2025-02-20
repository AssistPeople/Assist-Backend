package com.guesthouse.assistpeople.config;

import com.guesthouse.assistpeople.jwt.CustomUserDetailService;
import com.guesthouse.assistpeople.jwt.JwtAuthenticationFilter;
import com.guesthouse.assistpeople.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


        private final JwtTokenProvider jwtTokenProvider;
        private final CustomUserDetailService customUserDetailsService;



        private static final String[] AUTH_WHITELIST = {
                "/api/v1/member/**", "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
                "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html", "/api/v1/auth/**",
                "user/signup", "user/login",
        }; //더 열어둘 엔드포인트 여기에 추가


        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }



        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf((csrf)->csrf.disable());

            //세션 사용 안 함
            http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS));

            //FormLogin, BasicHttp 비활성화
            http.formLogin((form) -> form.disable());
            http.httpBasic(AbstractHttpConfigurer::disable);

//        //JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
            http.addFilterBefore(new JwtAuthenticationFilter(customUserDetailsService, jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);


            // 권한 규칙 작성
            http.authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(AUTH_WHITELIST).permitAll()
                    .anyRequest().authenticated()
            );

            return http.build();

        }
    }

