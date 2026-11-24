package com.umc.movie.dto;

// 로그인 성공 시 클라이언트에게 반환하는 데이터
public record LoginResponse(
        String nickname,    // 로그인한 회원의 닉네임
        String accessToken  // 서버가 발급한 JWT
) {
}