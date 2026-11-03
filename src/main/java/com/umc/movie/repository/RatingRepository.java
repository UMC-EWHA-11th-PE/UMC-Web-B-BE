package com.umc.movie.repository;

import com.umc.movie.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    // 특정 회원의 평점을 평점 ID 기준 내림차순(최신순)으로 조회 (5주차)
    List<Rating> findByMember_MemberIdOrderByRatingIdDesc(Long memberId);

    // 해당 회원이 특정 영화에 이미 평점을 등록했는지 확인 (6주차)
    boolean existsByMember_MemberIdAndMovieId(Long memberId, Long movieId);
}