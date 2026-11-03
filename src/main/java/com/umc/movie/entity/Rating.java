package com.umc.movie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity // MySQL의 rating 테이블과 연결
@Table(name = "rating")
@Getter // getter 메서드 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성
public class Rating {

    @Id // 기본키(PK)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private Long ratingId;

    // 평점을 작성한 회원과 연결 (Member : Rating = 1 : N)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // TMDB에서 제공하는 영화 ID
    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    // 영화 평점 (1~5점)
    @Column(nullable = false)
    private Byte score;

    // 영화에 대한 한줄평
    @Column(columnDefinition = "TEXT")
    private String comment;

    // 평점 생성 시간 (MySQL에서 자동 관리)
    @Column(name = "created_at", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // 평점 수정 시간 (MySQL에서 자동 관리)
    @Column(name = "updated_at", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}