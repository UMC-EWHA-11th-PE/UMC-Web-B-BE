package com.umc.movie.repository;

import com.umc.movie.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // 특정 회원의 전체 즐겨찾기 목록 조회
    List<Favorite> findByMember_MemberIdOrderByFavoriteIdDesc(Long memberId);

    // 특정 회원이 해당 영화를 즐겨찾기에 등록했는지 확인
    boolean existsByMember_MemberIdAndMovieId(Long memberId, Long movieId);

    // 특정 회원의 특정 영화 즐겨찾기 정보 조회
    Optional<Favorite> findByMember_MemberIdAndMovieId(
            Long memberId, Long movieId
    );
}