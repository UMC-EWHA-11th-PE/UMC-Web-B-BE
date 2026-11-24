package com.umc.movie.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite extends BaseTimeEntity {

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

    // 새로운 즐겨찾기 객체를 생성하기 위한 Builder 생성자
    @Builder
    public Favorite(Member member, Long movieId) {
        this.member = member;
        this.movieId = movieId;
    }
}