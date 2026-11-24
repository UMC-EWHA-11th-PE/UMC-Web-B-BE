package com.umc.movie.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // MySQL의 favorite 테이블과 연결
@Table(
        name = "favorite",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id", "movie_id"})
        }
)
@Getter
@NoArgsConstructor
public class Favorite {

    @Id // 기본키(PK)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long favoriteId;

    // 즐겨찾기를 등록한 회원과 연결 (Member : Favorite = 1 : N)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // TMDB에서 제공하는 영화 ID
    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    //즐겨찾기 생성 시간(MySQL에서 자동 관리)
    @Column(name = "created_at", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime createdAt;

    //즐겨찾기 수정 시간(MySQL에서 관리)
    @Column(name = "updated_at", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // 새로운 즐겨찾기 객체를 생성하기 위한 Builder 생성자
    @Builder
    public Favorite(Member member, Long movieId) {
        this.member = member;
        this.movieId = movieId;
    }
}