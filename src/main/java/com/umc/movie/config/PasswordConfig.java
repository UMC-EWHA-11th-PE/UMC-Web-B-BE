package com.umc.movie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Spring Boot 설정 클래스
@Configuration
public class PasswordConfig {

    // PasswordEncoder 객체를 Spring Bean으로 등록
    @Bean
    public PasswordEncoder passwordEncoder() {

        // bcrypt 알고리즘을 사용하는 PasswordEncoder 생성
        return new BCryptPasswordEncoder();
    }
}