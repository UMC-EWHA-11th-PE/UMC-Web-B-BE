package com.umc.movie.dto;

// 회원가입 성공 시 클라이언트에 반환할 데이터
public record MemberResponse(
        Long memberId,   // 회원 고유 ID
        String email,    // 이메일
        String nickname  // 닉네임
) {}