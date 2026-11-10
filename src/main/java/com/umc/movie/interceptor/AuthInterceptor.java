package com.umc.movie.interceptor;

import com.umc.movie.exception.ApiException;
import com.umc.movie.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    // Controller 실행 전에 JWT 검사
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {

        // CORS 사전 요청은 인증 검사 없이 통과
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 1. Authorization 헤더 가져오기
        String authorization = request.getHeader("Authorization");

        // 2. Bearer 토큰이 없거나 형식이 잘못되면 401 반환
        if (authorization == null ||
                !authorization.startsWith("Bearer ") ||
                authorization.substring(7).isBlank()) {

            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH401_2",
                    "인증 토큰이 필요합니다."
            );
        }

        // 3. Bearer 뒤의 JWT 문자열 추출
        String token = authorization.substring(7);

        // 4. JWT 검증 후 회원 ID 추출
        Long memberId = jwtUtil.getMemberId(token);

        // 5. Controller에서 사용할 수 있도록 회원 ID 저장
        request.setAttribute("memberId", memberId);

        // 인증 성공 → Controller 실행 허용
        return true;
    }
}