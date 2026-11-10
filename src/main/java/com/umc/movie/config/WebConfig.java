package com.umc.movie.config;

import com.umc.movie.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Spring Boot의 설정 클래스임을 표시
@RequiredArgsConstructor // final 필드 생성자 자동 생성
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    // 기존 5·6주차 CORS 설정 유지
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**") // 모든 API 경로에 CORS 설정 적용
                .allowedOrigins("http://localhost:5173") // React 개발 서버 허용
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }

    // JWT 인증 인터셉터 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**") // 기본적으로 모든 API에 JWT 인증 적용
                .excludePathPatterns(
                        "/auth/**",              // 로그인
                        "/members",              // 회원가입
                        "/members/nickname/**",  // 닉네임 중복 확인
                        "/members/email/**",     // 이메일 중복 확인
                        "/members/*/ratings",    // 회원별 평점 조회
                        "/error"                 // 오류 처리 경로
                );
    }
}