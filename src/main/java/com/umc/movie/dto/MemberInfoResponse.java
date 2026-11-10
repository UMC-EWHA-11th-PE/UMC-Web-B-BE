package com.umc.movie.dto;

import com.umc.movie.entity.Member;
import java.time.LocalDateTime;

// 내 정보 조회 시 클라이언트에게 반환할 데이터
public record MemberInfoResponse(
        Long memberId,
        String email,
        String nickname,
        LocalDateTime createdAt
) {

    // Member 엔티티를 응답 DTO로 변환
    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(
                member.getMemberId(),
                member.getEmail(),
                member.getNickname(),
                member.getCreatedAt()
        );
    }
}