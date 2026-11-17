
package com.umc.movie.repository;

import com.umc.movie.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    // 5주차: 특정 회원의 평점 목록을 최신순으로 조회
    List<Rating> findByMember_MemberIdOrderByRatingIdDesc(Long memberId);

    // 6주차: 특정 회원이 특정 영화에 이미 평점을 등록했는지 확인
    boolean existsByMember_MemberIdAndMovieId(Long memberId, Long movieId);
}
