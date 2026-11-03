package com.umc.movie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity // 이 클래스가 DB 테이블과 연결되는 엔티티임을 표시
@Table(name = "member") // MySQL의 member 테이블과 연결
@Getter // 필드 값을 읽는 getter 메서드 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성
public class Member {

    @Id // 기본키(PK)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(name = "created_at", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // 회원가입 시 Member 객체를 생성하기 위한 Builder 생성자
    @Builder
    public Member(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

}